package com.yangshengzhou.lucky_sms.service.student;

import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionResultVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
     * 分页获取可选课程列表
     * @param userId 学生ID
     * @param semester 学期
     * @param page 页码
     * @param size 每页数量
     * @return 分页的可选课程列表
     */
    Page<CourseSelectionVO> getAvailableCoursesWithPagination(Integer userId, String semester, Integer page, Integer size);
    
    /**
     * 分页获取已选课程列表
     * @param userId 学生ID
     * @param semester 学期
     * @param page 页码
     * @param size 每页数量
     * @return 分页的已选课程列表
     */
    Page<CourseSelectionVO> getSelectedCoursesWithPagination(Integer userId, String semester, Integer page, Integer size);
    
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