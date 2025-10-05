package com.yangshengzhou.lucky_sms.service.student.impl;

import com.yangshengzhou.lucky_sms.mapper.student.CourseMapper;
import com.yangshengzhou.lucky_sms.mapper.student.CourseSelectionMapper;
import com.yangshengzhou.lucky_sms.mapper.student.StudentMapper;
import com.yangshengzhou.lucky_sms.service.student.CourseSelectionService;
import com.yangshengzhou.lucky_sms.utils.RedisUtil;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionResultVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
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
    private RedisUtil redisUtil;

    private static final String COURSE_SELECTION_LOCK_PREFIX = "course_selection_lock:"; // Redis锁的key前缀（防并发）
    private static final String COURSE_CAPACITY_PREFIX = "course_capacity:"; // 课程容量在Redis中的key前缀
    private static final int LOCK_EXPIRE_TIME = 10; // Redis锁的过期时间（10秒，防止锁一直占着）
    
    @Override
    public List<CourseSelectionVO> getAvailableCourses(Integer userId, String semester) {
        return courseMapper.getAvailableCourses(userId, semester);
    }
    
    @Override
    public List<CourseSelectionVO> getSelectedCourses(Integer userId, String semester) {
        return courseSelectionMapper.getSelectedCourses(userId, semester);
    }

    // 抢课的关键代码
    @Override
    @Transactional
    public CourseSelectionResultVO selectCourse(Integer userId, Integer courseId) {
        CourseSelectionResultVO result = new CourseSelectionResultVO();
        
        // 获取Redis锁，防止并发问题
        String lockKey = COURSE_SELECTION_LOCK_PREFIX + courseId;
        boolean locked = false;
        
        try {
            // 尝试获取锁
            locked = redisUtil.getLock(lockKey, "1", LOCK_EXPIRE_TIME, TimeUnit.SECONDS);
            
            if (!locked) {
                result.setSuccess(false);
                result.setMessage("选课人数过多，请稍后再试");
                return result;
            }
            
            // 检查课程是否已选
            boolean alreadySelected = courseSelectionMapper.checkCourseSelected(userId, courseId);
            if (alreadySelected) {
                result.setSuccess(false);
                result.setMessage("您已经选择了该课程");
                return result;
            }
            
            // 检查课程容量，三目运算，先redis再mysql
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
            
            // 检查时间冲突
            boolean hasTimeConflict = courseSelectionMapper.checkTimeConflict(userId, courseId);
            if (hasTimeConflict) {
                result.setSuccess(false);
                result.setMessage("所选课程与已选课程时间冲突");
                return result;
            }
            
            // 检查学分上限
            Integer totalCredits = courseSelectionMapper.getTotalCredits(userId);
            Integer courseCredits = courseMapper.getCourseCredits(courseId);
            Integer maxCredits = studentMapper.getMaxCredits(userId);
            
            if (totalCredits + courseCredits > maxCredits) {
                result.setSuccess(false);
                result.setMessage("已达到学分上限，无法选择更多课程");
                return result;
            }
            
            // 执行选课
            boolean selectionSuccess = courseSelectionMapper.addCourseSelection(userId, courseId);
            
            if (selectionSuccess) {
                // 更新Redis中的课程容量
                redisUtil.increment(capacityKey, 1);
                
                // 获取课程信息
                CourseSelectionVO course = courseMapper.getCourseById(courseId);
                
                result.setSuccess(true);
                result.setMessage("选课成功");
                result.setCourse(course);
            } else {
                result.setSuccess(false);
                result.setMessage("选课失败，请稍后再试");
            }
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("系统错误：" + e.getMessage());
        } finally {
            // 释放锁
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
        
        // 获取Redis锁，防止并发问题
        String lockKey = COURSE_SELECTION_LOCK_PREFIX + courseId;
        boolean locked = false;
        
        try {
            // 尝试获取锁
            locked = redisUtil.getLock(lockKey, "1", LOCK_EXPIRE_TIME, TimeUnit.SECONDS);
            
            if (!locked) {
                result.setSuccess(false);
                result.setMessage("操作人数过多，请稍后再试");
                return result;
            }
            
            // 检查是否已选该课程
            boolean alreadySelected = courseSelectionMapper.checkCourseSelected(userId, courseId);
            if (!alreadySelected) {
                result.setSuccess(false);
                result.setMessage("您未选择该课程，无法退课");
                return result;
            }
            
            // 执行退课
            boolean dropSuccess = courseSelectionMapper.removeCourseSelection(userId, courseId);
            
            if (dropSuccess) {
                // 更新Redis中的课程容量
                String capacityKey = COURSE_CAPACITY_PREFIX + courseId;
                redisUtil.decrement(capacityKey, 1);
                
                result.setSuccess(true);
                result.setMessage("退课成功");
            } else {
                result.setSuccess(false);
                result.setMessage("退课失败，请稍后再试");
            }
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("系统错误：" + e.getMessage());
        } finally {
            // 释放锁
            if (locked) {
                redisUtil.releaseLock(lockKey, "1");
            }
        }
        
        return result;
    }
}