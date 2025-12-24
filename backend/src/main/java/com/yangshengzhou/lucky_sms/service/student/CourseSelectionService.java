package com.yangshengzhou.lucky_sms.service.student;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.pojo.CourseSelection;
import com.yangshengzhou.lucky_sms.vo.course.CourseSelectionVO;
import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionResultVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程选择服务接口 - 重构版
 */
public interface CourseSelectionService {
    
    /**
     * 获取学生可选课程列表
     * @param studentId 学生ID
     * @param semesterId 学期ID
     * @return 可选课程列表
     */
    List<CourseSelectionVO> getAvailableCourses(Integer studentId, Integer semesterId);
    
    /**
     * 获取学生已选课程列表
     * @param studentId 学生ID
     * @param semesterId 学期ID
     * @return 已选课程列表
     */
    List<CourseSelectionVO> getSelectedCourses(Integer studentId, Integer semesterId);
    
    /**
     * 分页获取可选课程列表
     * @param studentId 学生ID
     * @param semesterId 学期ID
     * @param page 页码
     * @param size 每页数量
     * @return 分页的可选课程列表
     */
    Page<CourseSelectionVO> getAvailableCoursesWithPagination(Integer studentId, Integer semesterId, Integer page, Integer size);
    
    /**
     * 分页获取已选课程列表
     * @param studentId 学生ID
     * @param semesterId 学期ID
     * @param page 页码
     * @param size 每页数量
     * @return 分页的已选课程列表
     */
    Page<CourseSelectionVO> getSelectedCoursesWithPagination(Integer studentId, Integer semesterId, Integer page, Integer size);
    
    /**
     * 选课
     * @param studentId 学生ID
     * @param assignmentId 教学安排ID
     * @param selectionTypeId 选课类型ID
     * @return 选课结果
     */
    CourseSelectionResultVO selectCourse(Integer studentId, Integer assignmentId, Integer selectionTypeId);
    
    /**
     * 退课
     * @param studentId 学生ID
     * @param selectionId 选课记录ID
     * @param dropReason 退课原因
     * @return 退课结果
     */
    CourseSelectionResultVO dropCourse(Integer studentId, Integer selectionId, String dropReason);
    
    /**
     * 批量选课
     * @param studentId 学生ID
     * @param assignmentIds 教学安排ID列表
     * @param selectionTypeId 选课类型ID
     * @return 选课结果列表
     */
    List<CourseSelectionResultVO> batchSelectCourses(Integer studentId, List<Integer> assignmentIds, Integer selectionTypeId);
    
    /**
     * 检查选课条件
     * @param studentId 学生ID
     * @param assignmentId 教学安排ID
     * @return 检查结果
     */
    CourseSelectionResultVO checkSelectionEligibility(Integer studentId, Integer assignmentId);
    
    /**
     * 获取学生选课统计
     * @param studentId 学生ID
     * @param semesterId 学期ID
     * @return 选课统计信息
     */
    CourseSelectionStatisticsVO getStudentSelectionStatistics(Integer studentId, Integer semesterId);
    
    /**
     * 更新选课状态
     * @param selectionId 选课记录ID
     * @param statusId 状态ID
     * @return 是否成功
     */
    boolean updateSelectionStatus(Integer selectionId, Integer statusId);
    
    /**
     * 获取选课时间范围内的课程
     * @param studentId 学生ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 课程列表
     */
    List<CourseSelectionVO> getCoursesByTimeRange(Integer studentId, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 检查时间冲突
     * @param studentId 学生ID
     * @param assignmentId 教学安排ID
     * @return 冲突信息
     */
    TimeConflictVO checkTimeConflict(Integer studentId, Integer assignmentId);
    
    /**
     * 获取课程选择详情
     * @param selectionId 选课记录ID
     * @return 选课详情
     */
    CourseSelectionVO getSelectionDetail(Integer selectionId);
    
    /**
     * 分页获取教学安排的学生列表
     * @param assignmentId 教学安排ID
     * @param page 页码
     * @param size 每页数量
     * @return 学生列表
     */
    Page<CourseSelectionVO> getAssignmentStudents(Integer assignmentId, Integer page, Integer size);
    
    /**
     * 导出学生选课信息
     * @param studentId 学生ID
     * @param semesterId 学期ID
     * @return 导出数据
     */
    byte[] exportStudentSelections(Integer studentId, Integer semesterId);
}