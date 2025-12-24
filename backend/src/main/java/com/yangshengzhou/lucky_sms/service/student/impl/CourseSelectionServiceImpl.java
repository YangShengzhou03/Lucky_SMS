package com.yangshengzhou.lucky_sms.service.student.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.pojo.CourseSelection;
import com.yangshengzhou.lucky_sms.mapper.student.StudentCourseMapper;
import com.yangshengzhou.lucky_sms.mapper.student.StudentCourseSelectionMapper;
import com.yangshengzhou.lucky_sms.mapper.student.StudentViewMapper;
import com.yangshengzhou.lucky_sms.service.RocketMQProducerService;
import com.yangshengzhou.lucky_sms.service.student.CourseSelectionService;
import com.yangshengzhou.lucky_sms.utils.RedisUtil;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionResultVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionStatisticsVO;
import com.yangshengzhou.lucky_sms.vo.student.TimeConflictVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Resource
    private StudentCourseMapper studentCourseMapper;
    
    @Resource
    private StudentCourseSelectionMapper studentCourseSelectionMapper;
    
    @Resource
    private StudentViewMapper studentViewMapper;

    @Resource
    private RocketMQProducerService rocketMQProducerService;
    
    @Resource
    private RedisUtil redisUtil;

    private static final String COURSE_SELECTION_LOCK_PREFIX = "course_selection_lock:";
    private static final String COURSE_CAPACITY_PREFIX = "course_capacity:";
    private static final int LOCK_EXPIRE_TIME = 10;
    
    @Override
    public List<CourseSelectionVO> getAvailableCourses(Integer studentId, Integer semesterId) {
        return studentCourseMapper.getAvailableCourses(studentId, semesterId.toString());
    }
    
    @Override
    public List<CourseSelectionVO> getSelectedCourses(Integer studentId, Integer semesterId) {
        return studentCourseSelectionMapper.getSelectedCourses(studentId, semesterId.toString());
    }

    @Override
    public Page<CourseSelectionVO> getAvailableCoursesWithPagination(Integer studentId, Integer semesterId, Integer page, Integer size) {
        Page<CourseSelectionVO> pageInfo = new Page<>(page, size);
        return studentCourseMapper.getAvailableCoursesWithPagination(pageInfo, studentId, semesterId.toString());
    }

    @Override
    public Page<CourseSelectionVO> getSelectedCoursesWithPagination(Integer studentId, Integer semesterId, Integer page, Integer size) {
        Page<CourseSelectionVO> pageInfo = new Page<>(page, size);
        return studentCourseSelectionMapper.getSelectedCoursesWithPagination(pageInfo, studentId, semesterId.toString());
    }

    @Override
    @Transactional
    public CourseSelectionResultVO selectCourse(Integer studentId, Integer assignmentId, Integer selectionTypeId) {
        CourseSelectionResultVO result = new CourseSelectionResultVO();
        
        String lockKey = COURSE_SELECTION_LOCK_PREFIX + assignmentId;
        boolean locked = false;
        
        try {
            locked = redisUtil.getLock(lockKey, "1", LOCK_EXPIRE_TIME, TimeUnit.SECONDS);
            
            if (!locked) {
                result.setSuccess(false);
                result.setMessage("选课人数过多，请稍后再试");
                return result;
            }
            
            boolean alreadySelected = studentCourseSelectionMapper.checkCourseSelected(studentId, assignmentId);
            if (alreadySelected) {
                result.setSuccess(false);
                result.setMessage("您已经选择了该课程");
                return result;
            }
            
            String capacityKey = COURSE_CAPACITY_PREFIX + assignmentId;
            Integer currentStudents = redisUtil.get(capacityKey) != null ? 
                Integer.parseInt(redisUtil.get(capacityKey).toString()) : 
                studentCourseMapper.getCurrentStudents(assignmentId);
            
            Integer maxStudents = studentCourseMapper.getMaxStudents(assignmentId);
            
            if (currentStudents >= maxStudents) {
                result.setSuccess(false);
                result.setMessage("课程已满，无法选择");
                return result;
            }
            
            boolean selectionSuccess = studentCourseSelectionMapper.addCourseSelection(studentId, assignmentId);
            
            if (selectionSuccess) {
                redisUtil.increment(capacityKey, 1);
                
                CourseSelectionVO course = studentCourseMapper.getCourseById(assignmentId);
                
                result.setSuccess(true);
                result.setMessage("选课成功");
                result.setCourse(course);
                
                Map<String, Object> message = new HashMap<>();
                message.put("userId", studentId);
                message.put("assignmentId", assignmentId);
                message.put("courseName", course.getCourseName());
                message.put("operationTime", System.currentTimeMillis());
                rocketMQProducerService.sendCourseSelectionSuccessMessage(message);
            } else {
                result.setSuccess(false);
                result.setMessage("选课失败，请稍后再试");
            }
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("系统错误：" + e.getMessage());
        } finally {
            if (locked) {
                redisUtil.releaseLock(lockKey, "1");
            }
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public CourseSelectionResultVO dropCourse(Integer studentId, Integer selectionId, String dropReason) {
        CourseSelectionResultVO result = new CourseSelectionResultVO();
        

        CourseSelection selection = studentCourseSelectionMapper.selectById(selectionId);
        if (selection == null) {
            result.setSuccess(false);
            result.setMessage("选课记录不存在");
            return result;
        }
        
        Integer assignmentId = selection.getAssignmentId();
        
        String lockKey = COURSE_SELECTION_LOCK_PREFIX + assignmentId;
        boolean locked = false;
        
        try {
            locked = redisUtil.getLock(lockKey, "1", LOCK_EXPIRE_TIME, TimeUnit.SECONDS);
            
            if (!locked) {
                result.setSuccess(false);
                result.setMessage("操作人数过多，请稍后再试");
                return result;
            }
            
            boolean alreadySelected = studentCourseSelectionMapper.checkCourseSelected(studentId, assignmentId);
            if (!alreadySelected) {
                result.setSuccess(false);
                result.setMessage("您未选择该课程，无法退课");
                return result;
            }
            
            boolean dropSuccess = studentCourseSelectionMapper.removeCourseSelection(studentId, assignmentId);
            
            if (dropSuccess) {
                String capacityKey = COURSE_CAPACITY_PREFIX + assignmentId;
                redisUtil.decrement(capacityKey, 1);
                
                result.setSuccess(true);
                result.setMessage("退课成功");
                
                Map<String, Object> message = new HashMap<>();
                message.put("userId", studentId);
                message.put("assignmentId", assignmentId);
                message.put("selectionId", selectionId);
                message.put("dropReason", dropReason);
                message.put("operationTime", System.currentTimeMillis());
                rocketMQProducerService.sendDropCourseSuccessMessage(message);
            } else {
                result.setSuccess(false);
                result.setMessage("退课失败，请稍后再试");
            }
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("系统错误：" + e.getMessage());
        } finally {
            if (locked) {
                redisUtil.releaseLock(lockKey, "1");
            }
        }
        
        return result;
    }
    
    @Override
    public List<CourseSelectionResultVO> batchSelectCourses(Integer studentId, List<Integer> assignmentIds, Integer selectionTypeId) {

        throw new UnsupportedOperationException("Batch selection not implemented yet");
    }
    
    @Override
    public CourseSelectionResultVO checkSelectionEligibility(Integer studentId, Integer assignmentId) {

        throw new UnsupportedOperationException("Eligibility check not implemented yet");
    }
    
    @Override
    public CourseSelectionStatisticsVO getStudentSelectionStatistics(Integer studentId, Integer semesterId) {

        throw new UnsupportedOperationException("Statistics not implemented yet");
    }
    
    @Override
    public boolean updateSelectionStatus(Integer selectionId, Integer statusId) {

        throw new UnsupportedOperationException("Status update not implemented yet");
    }
    
    @Override
    public List<CourseSelectionVO> getCoursesByTimeRange(Integer studentId, LocalDateTime startTime, LocalDateTime endTime) {

        throw new UnsupportedOperationException("Time range query not implemented yet");
    }
    
    @Override
    public TimeConflictVO checkTimeConflict(Integer studentId, Integer assignmentId) {

        throw new UnsupportedOperationException("Time conflict check not implemented yet");
    }
    
    @Override
    public CourseSelectionVO getSelectionDetail(Integer selectionId) {

        throw new UnsupportedOperationException("Selection detail not implemented yet");
    }
    
    @Override
    public Page<CourseSelectionVO> getAssignmentStudents(Integer assignmentId, Integer page, Integer size) {

        throw new UnsupportedOperationException("Assignment students not implemented yet");
    }
    
    @Override
    public byte[] exportStudentSelections(Integer studentId, Integer semesterId) {

        throw new UnsupportedOperationException("Export not implemented yet");
    }
}