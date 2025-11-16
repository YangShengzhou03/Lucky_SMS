package com.yangshengzhou.lucky_sms.mapper.student;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.pojo.CourseSelection;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 选课记录Mapper接口
 */
public interface CourseSelectionMapper extends BaseMapper<CourseSelection> {
    /**
     * 获取已选课程列表
     * @param userId 学生ID
     * @param semester 学期
     * @return 已选课程列表
     */
    List<CourseSelectionVO> getSelectedCourses(@Param("userId") Integer userId, @Param("semester") String semester);
    
    /**
     * 分页获取已选课程列表
     * @param page 分页参数
     * @param userId 学生ID
     * @param semester 学期
     * @return 分页的已选课程列表
     */
    Page<CourseSelectionVO> getSelectedCoursesWithPagination(Page<CourseSelectionVO> page, @Param("userId") Integer userId, @Param("semester") String semester);
    
    /**
     * 检查是否已选某课程
     * @param userId 学生ID
     * @param courseId 课程ID
     * @return 是否已选
     */
    Boolean checkCourseSelected(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    /**
     * 检查时间冲突
     * @param userId 学生ID
     * @param courseId 课程ID
     * @return 是否有时间冲突
     */
    Boolean checkTimeConflict(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    /**
     * 获取已选课程总学分
     * @param userId 学生ID
     * @return 总学分
     */
    Integer getTotalCredits(@Param("userId") Integer userId);
    
    /**
     * 添加选课记录
     * @param userId 学生ID
     * @param courseId 课程ID
     * @return 是否添加成功
     */
    Boolean addCourseSelection(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
    
    /**
     * 删除选课记录
     * @param userId 学生ID
     * @param courseId 课程ID
     * @return 是否删除成功
     */
    Boolean removeCourseSelection(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
}