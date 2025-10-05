package com.yangshengzhou.lucky_sms.mapper.student;

import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
    /**
     * 获取可选课程列表
     * @param userId 学生ID
     * @param semester 学期
     * @return 可选课程列表
     */
    List<CourseSelectionVO> getAvailableCourses(@Param("userId") Integer userId, @Param("semester") String semester);
    
    /**
     * 根据课程ID获取课程信息
     * @param courseId 课程ID
     * @return 课程信息
     */
    CourseSelectionVO getCourseById(@Param("courseId") Integer courseId);
    
    /**
     * 获取课程最大容量
     * @param courseId 课程ID
     * @return 最大容量
     */
    Integer getMaxStudents(@Param("courseId") Integer courseId);
    
    /**
     * 获取课程当前已选人数
     * @param courseId 课程ID
     * @return 当前已选人数
     */
    Integer getCurrentStudents(@Param("courseId") Integer courseId);
    
    /**
     * 获取课程学分
     * @param courseId 课程ID
     * @return 课程学分
     */
    Integer getCourseCredits(@Param("courseId") Integer courseId);
}