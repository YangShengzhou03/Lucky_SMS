package com.yangshengzhou.lucky_sms.service.student.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.mapper.student.CourseMapper;
import com.yangshengzhou.lucky_sms.mapper.student.CourseSelectionMapper;
import com.yangshengzhou.lucky_sms.mapper.student.StudentMapper;
import com.yangshengzhou.lucky_sms.service.RocketMQProducerService;
import com.yangshengzhou.lucky_sms.service.student.CourseSelectionService;
import com.yangshengzhou.lucky_sms.utils.RedisUtil;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionResultVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Resource
    private CourseMapper courseMapper;
    
    @Resource
    private CourseSelectionMapper courseSelectionMapper;
    
    @Resource
    private StudentMapper studentMapper;

    @Resource
    private RocketMQProducerService rocketMQProducerService;
    
    @Resource
    private RedisUtil redisUtil;

    private static final String COURSE_SELECTION_LOCK_PREFIX = "course_selection_lock:";
    private static final String COURSE_CAPACITY_PREFIX = "course_capacity:";
    private static final int LOCK_EXPIRE_TIME = 10;
    
    @Override
    public List<CourseSelectionVO> getAvailableCourses(Integer userId, String semester) {
        return courseMapper.getAvailableCourses(userId, semester);
    }
    
    @Override
    public List<CourseSelectionVO> getSelectedCourses(Integer userId, String semester) {
        return courseSelectionMapper.getSelectedCourses(userId, semester);
    }

    @Override
    public Page<CourseSelectionVO> getAvailableCoursesWithPagination(Integer userId, String semester, Integer page, Integer size) {
        Page<CourseSelectionVO> pageInfo = new Page<>(page, size);
        return courseMapper.getAvailableCoursesWithPagination(pageInfo, userId, semester);
    }

    @Override
    public Page<CourseSelectionVO> getSelectedCoursesWithPagination(Integer userId, String semester, Integer page, Integer size) {
        Page<CourseSelectionVO> pageInfo = new Page<>(page, size);
        return courseSelectionMapper.getSelectedCoursesWithPagination(pageInfo, userId, semester);
    }

    @Override
    @Transactional
    public CourseSelectionResultVO selectCourse(Integer userId, Integer courseId) {
        CourseSelectionResultVO result = new CourseSelectionResultVO();
        
        String lockKey = COURSE_SELECTION_LOCK_PREFIX + courseId;
        boolean locked = false;
        
        try {
            locked = redisUtil.getLock(lockKey, "1", LOCK_EXPIRE_TIME, TimeUnit.SECONDS);
            
            if (!locked) {
                result.setSuccess(false);
                result.setMessage("选课人数过多，请稍后再试");
                return result;
            }
            
            boolean alreadySelected = courseSelectionMapper.checkCourseSelected(userId, courseId);
            if (alreadySelected) {
                result.setSuccess(false);
                result.setMessage("您已经选择了该课程");
                return result;
            }
            
            String capacityKey = COURSE_CAPACITY_PREFIX + courseId;
            Integer currentStudents = redisUtil.get(capacityKey) != null ? 
                Integer.parseInt(redisUtil.get(capacityKey).toString()) : 
                courseMapper.getCurrentStudents(courseId);
            
            Integer maxStudents = courseMapper.getMaxStudents(courseId);
            
            if (currentStudents >= maxStudents) {
                result.setSuccess(false);
                result.setMessage("课程已满，无法选择");
                return result;
            }
            
            boolean selectionSuccess = courseSelectionMapper.addCourseSelection(userId, courseId);
            
            if (selectionSuccess) {
                redisUtil.increment(capacityKey, 1);
                
                CourseSelectionVO course = courseMapper.getCourseById(courseId);
                
                result.setSuccess(true);
                result.setMessage("选课成功");
                result.setCourse(course);
                
                Map<String, Object> message = new HashMap<>();
                message.put("userId", userId);
                message.put("courseId", courseId);
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
    public CourseSelectionResultVO dropCourse(Integer userId, Integer courseId) {
        CourseSelectionResultVO result = new CourseSelectionResultVO();
        
        String lockKey = COURSE_SELECTION_LOCK_PREFIX + courseId;
        boolean locked = false;
        
        try {
            locked = redisUtil.getLock(lockKey, "1", LOCK_EXPIRE_TIME, TimeUnit.SECONDS);
            
            if (!locked) {
                result.setSuccess(false);
                result.setMessage("操作人数过多，请稍后再试");
                return result;
            }
            
            boolean alreadySelected = courseSelectionMapper.checkCourseSelected(userId, courseId);
            if (!alreadySelected) {
                result.setSuccess(false);
                result.setMessage("您未选择该课程，无法退课");
                return result;
            }
            
            boolean dropSuccess = courseSelectionMapper.removeCourseSelection(userId, courseId);
            
            if (dropSuccess) {
                String capacityKey = COURSE_CAPACITY_PREFIX + courseId;
                redisUtil.decrement(capacityKey, 1);
                
                result.setSuccess(true);
                result.setMessage("退课成功");
                
                Map<String, Object> message = new HashMap<>();
                message.put("userId", userId);
                message.put("courseId", courseId);
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
}