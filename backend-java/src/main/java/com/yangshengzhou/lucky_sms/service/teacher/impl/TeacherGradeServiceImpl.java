package com.yangshengzhou.lucky_sms.service.teacher.impl;

import com.yangshengzhou.lucky_sms.mapper.teacher.TeacherViewMapper;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherGradeService;
import com.yangshengzhou.lucky_sms.vo.teacher.GradeManagementVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class TeacherGradeServiceImpl implements TeacherGradeService {

    @Resource
    private TeacherViewMapper teacherViewMapper;

    @Override
    public GradeManagementVO getCourseGrades(Integer courseId, Integer teacherId) {
        return teacherViewMapper.getCourseGrades(courseId, teacherId);
    }

    @Override
    public boolean updateGrade(Integer gradeId, Double usualScore, Double midtermScore, Double finalScore, Integer teacherId) {
        int result = teacherViewMapper.updateGrade(gradeId, usualScore, midtermScore, finalScore);
        return result > 0;
    }
}
