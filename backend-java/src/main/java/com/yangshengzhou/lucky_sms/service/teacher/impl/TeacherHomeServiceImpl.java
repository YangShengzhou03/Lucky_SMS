package com.yangshengzhou.lucky_sms.service.teacher.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangshengzhou.lucky_sms.mapper.TeacherMapper;
import com.yangshengzhou.lucky_sms.mapper.teacher.TeacherViewMapper;
import com.yangshengzhou.lucky_sms.pojo.Teacher;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherHomeService;
import com.yangshengzhou.lucky_sms.vo.teacher.HomeVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class TeacherHomeServiceImpl implements TeacherHomeService {

    @Resource
    private TeacherViewMapper teacherViewMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public HomeVO getHomeData(Integer userId) {
        HomeVO homeVO = new HomeVO();

        com.yangshengzhou.lucky_sms.vo.teacher.TeacherProfileVO profile = teacherViewMapper.selectTeacherProfileById(userId);
        if (profile != null) {
            homeVO.setTeacherName(profile.getName());
            homeVO.setTeacherId(profile.getId());
        }

        Teacher teacher = teacherMapper.selectOne(new QueryWrapper<Teacher>().eq("user_id", userId));
        if (teacher != null) {
            Integer teacherId = teacher.getTeacherId();
            homeVO.setTotalStudents(teacherViewMapper.getTotalStudents(teacherId));
            homeVO.setTotalCourses(teacherViewMapper.getTotalCourses(teacherId));
            homeVO.setThisSemesterCourses(teacherViewMapper.getThisSemesterCourses(teacherId, "2024-2025-第一学期"));
        }

        homeVO.setRecentMessages(5);
        homeVO.setPendingTasks(2);

        return homeVO;
    }
}
