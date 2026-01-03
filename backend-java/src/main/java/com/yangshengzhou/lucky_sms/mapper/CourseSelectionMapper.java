package com.yangshengzhou.lucky_sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.pojo.CourseSelection;
import com.yangshengzhou.lucky_sms.vo.course.CourseSelectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程选择Mapper接口 - 扩展版
 */
@Mapper
public interface CourseSelectionMapper extends BaseMapper<CourseSelection> {
    
    /**
     * 获取学生的选课列表（带详细信息）
     */
    IPage<CourseSelectionVO> getStudentCourseSelections(Page<CourseSelectionVO> page, @Param("studentId") Integer studentId);
    
    /**
     * 获取教学安排的学生列表
     */
    List<CourseSelectionVO> getAssignmentStudents(@Param("assignmentId") Integer assignmentId);
    
    /**
     * 检查学生是否已经选了某门课程
     */
    boolean isCourseSelected(@Param("studentId") Integer studentId, @Param("assignmentId") Integer assignmentId);
    
    /**
     * 获取学生的选课数量
     */
    int getStudentCourseCount(@Param("studentId") Integer studentId, @Param("semesterId") Integer semesterId);
    
    /**
     * 获取教学安排的当前选课人数
     */
    int getAssignmentCurrentStudents(@Param("assignmentId") Integer assignmentId);
    
    /**
     * 批量更新选课状态
     */
    int batchUpdateStatus(@Param("selectionIds") List<Integer> selectionIds, @Param("statusId") Integer statusId);
}