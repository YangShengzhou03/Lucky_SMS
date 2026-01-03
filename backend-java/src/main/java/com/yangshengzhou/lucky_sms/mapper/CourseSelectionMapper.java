package com.yangshengzhou.lucky_sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.pojo.CourseSelection;
import com.yangshengzhou.lucky_sms.vo.course.CourseSelectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseSelectionMapper extends BaseMapper<CourseSelection> {
    
    IPage<CourseSelectionVO> getStudentCourseSelections(Page<CourseSelectionVO> page, @Param("studentId") Integer studentId);
    
    List<CourseSelectionVO> getAssignmentStudents(@Param("assignmentId") Integer assignmentId);
    
    boolean isCourseSelected(@Param("studentId") Integer studentId, @Param("assignmentId") Integer assignmentId);
    
    int getStudentCourseCount(@Param("studentId") Integer studentId, @Param("semesterId") Integer semesterId);
    
    int getAssignmentCurrentStudents(@Param("assignmentId") Integer assignmentId);
    
    int batchUpdateStatus(@Param("selectionIds") List<Integer> selectionIds, @Param("statusId") Integer statusId);
}