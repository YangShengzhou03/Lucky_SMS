package com.yangshengzhou.lucky_sms.service.teacher.impl;

import com.yangshengzhou.lucky_sms.mapper.teacher.TeacherViewMapper;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherProfileService;
import com.yangshengzhou.lucky_sms.vo.teacher.TeacherProfileVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class TeacherProfileServiceImpl implements TeacherProfileService {

    @Resource
    private TeacherViewMapper teacherViewMapper;

    @Override
    public TeacherProfileVO getTeacherProfile(Integer userId) {
        return teacherViewMapper.selectTeacherProfileById(userId);
    }

    @Override
    public boolean updateTeacherProfile(Integer userId, TeacherProfileVO profile) {
        try {
            int result = teacherViewMapper.updateTeacherProfile(
                userId,
                profile.getEmail(),
                profile.getPhone(),
                profile.getEducation()
            );
            return result > 0;
        } catch (Exception e) {
            System.err.println("更新教师个人信息失败: userId=" + userId + ", error=" + e.getMessage());
            return false;
        }
    }
}
