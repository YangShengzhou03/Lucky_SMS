package com.yangshengzhou.lucky_sms.mapper.student;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.pojo.Course;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentCourseMapper extends BaseMapper<Course> {
    List<CourseSelectionVO> getAvailableCourses(@Param("userId") Integer userId, @Param("semester") String semester);
    
    Page<CourseSelectionVO> getAvailableCoursesWithPagination(Page<CourseSelectionVO> page, @Param("userId") Integer userId, @Param("semester") String semester);
    
    CourseSelectionVO getCourseById(@Param("courseId") Integer courseId);
    
    Integer getMaxStudents(@Param("courseId") Integer courseId);
    
    Integer getCurrentStudents(@Param("courseId") Integer courseId);
    
    Integer getCourseCredits(@Param("courseId") Integer courseId);
}