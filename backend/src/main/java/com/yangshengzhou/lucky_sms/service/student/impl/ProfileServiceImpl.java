package com.yangshengzhou.lucky_sms.service.student.impl;

import com.yangshengzhou.lucky_sms.mapper.student.StudentViewMapper;
import com.yangshengzhou.lucky_sms.service.student.ProfileService;
import com.yangshengzhou.lucky_sms.vo.student.StudentProfileVO;
import com.yangshengzhou.lucky_sms.vo.student.UpdateStudentProfileRequestVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class ProfileServiceImpl implements ProfileService {
    
    @Resource
    private StudentViewMapper studentViewMapper;
    
    @Override
    public StudentProfileVO getStudentProfile(Integer userId) {
        return studentViewMapper.selectStudentProfileById(userId);
    }
    
    @Override
    public boolean updateStudentProfile(Integer userId, UpdateStudentProfileRequestVO requestVO) {
        try {
            String birthDate = null;
            if (requestVO.getBirthDate() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                birthDate = sdf.format(requestVO.getBirthDate());
            }
            

            String gender = requestVO.getGender();
            
            int result = studentViewMapper.updateStudentProfile(
                userId,
                requestVO.getUsername(),
                requestVO.getEmail(),
                requestVO.getPhone(),
                gender,
                birthDate,
                requestVO.getEmergencyContact(),
                requestVO.getEmergencyPhone()
            );
            
            return result > 0;
        } catch (Exception e) {
            System.err.println("更新学生个人信息失败: userId=" + userId + ", error=" + e.getMessage());
            return false;
        }
    }
}