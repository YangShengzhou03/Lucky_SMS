package com.yangshengzhou.lucky_sms.service.teacher.impl;

import com.yangshengzhou.lucky_sms.mapper.teacher.TeacherViewMapper;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherScheduleService;
import com.yangshengzhou.lucky_sms.vo.teacher.ScheduleVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class TeacherScheduleServiceImpl implements TeacherScheduleService {

    @Resource
    private TeacherViewMapper teacherViewMapper;

    @Override
    public ScheduleVO getSchedule(String semester, Integer teacherId) {
        return teacherViewMapper.getSchedule(teacherId, semester);
    }
}
