package com.yangshengzhou.lucky_sms.mapper.student;

import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseSelectionMapper {
    /**
     * 获取已选课程列表
     * @param userId 学生ID
     * @param semester 学期
     * @return 已选课程列表
     */
    List<CourseSelectionVO> getSelectedCourses(@Param("userId") Integer userId, @Param("semester") String semester);
    
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