package com.yangshengzhou.lucky_sms.service.teacher;

import com.yangshengzhou.lucky_sms.vo.teacher.GradeManagementVO;

/**
 * 教师成绩管理服务接口
 */
public interface TeacherGradeService {
    
    /**
     * 获取课程学生成绩
     * @param courseId 课程ID
     * @param teacherId 教师ID
     * @return 成绩列表
     */
    GradeManagementVO getCourseGrades(Integer courseId, Integer teacherId);
    
    /**
     * 更新学生成绩
     * @param gradeId 成绩ID
     * @param usualScore 平时成绩
     * @param midtermScore 期中成绩
     * @param finalScore 期末成绩
     * @param teacherId 教师ID
     * @return 是否成功
     */
    boolean updateGrade(Integer gradeId, Double usualScore, Double midtermScore, Double finalScore, Integer teacherId);
}
