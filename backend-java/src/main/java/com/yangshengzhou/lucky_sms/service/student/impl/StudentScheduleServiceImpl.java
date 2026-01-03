package com.yangshengzhou.lucky_sms.service.student.impl;

import com.yangshengzhou.lucky_sms.mapper.student.StudentViewMapper;
import com.yangshengzhou.lucky_sms.service.student.StudentScheduleService;
import com.yangshengzhou.lucky_sms.vo.teacher.ScheduleVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class StudentScheduleServiceImpl implements StudentScheduleService {

    @Resource
    private StudentViewMapper studentViewMapper;

    @Override
    public ScheduleVO getSchedule(String semester, Integer studentId) {
        return studentViewMapper.getStudentSchedule(studentId, semester);
    }
}
