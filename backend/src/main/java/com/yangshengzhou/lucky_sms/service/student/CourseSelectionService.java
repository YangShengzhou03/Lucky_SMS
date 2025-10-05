package com.yangshengzhou.lucky_sms.service.student;

import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionResultVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;

import java.util.List;

public interface CourseSelectionService {
    /**
     * 获取可选课程列表
     * @param userId 学生ID
     * @param semester 学期
     * @return 可选课程列表
     */
    List<CourseSelectionVO> getAvailableCourses(Integer userId, String semester);
    
    /**
     * 获取已选课程列表
     * @param userId 学生ID
     * @param semester 学期
     * @return 已选课程列表
     */
    List<CourseSelectionVO> getSelectedCourses(Integer userId, String semester);
    
    /**
     * 选课
     * @param userId 学生ID
     * @param courseId 课程ID
     * @return 选课结果
     */
    CourseSelectionResultVO selectCourse(Integer userId, Integer courseId);
    
    /**
     * 退课
     * @param userId 学生ID
     * @param courseId 课程ID
     * @return 退课结果
     */
    CourseSelectionResultVO dropCourse(Integer userId, Integer courseId);
}