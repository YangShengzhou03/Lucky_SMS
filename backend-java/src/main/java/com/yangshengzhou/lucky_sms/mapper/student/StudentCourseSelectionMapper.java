package com.yangshengzhou.lucky_sms.mapper.student;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.pojo.CourseSelection;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentCourseSelectionMapper extends BaseMapper<CourseSelection> {
    List<CourseSelectionVO> getSelectedCourses(@Param("userId") Integer userId, @Param("semester") String semester);
    
    Page<CourseSelectionVO> getSelectedCoursesWithPagination(Page<CourseSelectionVO> page, @Param("userId") Integer userId, @Param("semester") String semester);
    
    Boolean checkCourseSelected(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    Boolean checkTimeConflict(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    Integer getTotalCredits(@Param("userId") Integer userId);
    
    Boolean addCourseSelection(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    Boolean removeCourseSelection(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
}