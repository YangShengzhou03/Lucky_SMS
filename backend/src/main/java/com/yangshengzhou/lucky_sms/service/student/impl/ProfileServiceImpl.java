package com.yangshengzhou.lucky_sms.service.student.impl;

import com.yangshengzhou.lucky_sms.mapper.student.StudentMapper;
import com.yangshengzhou.lucky_sms.service.student.ProfileService;
import com.yangshengzhou.lucky_sms.vo.student.StudentProfileVO;
import com.yangshengzhou.lucky_sms.vo.student.UpdateStudentProfileRequestVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 学生个人信息服务实现类
 */
@Service
public class ProfileServiceImpl implements ProfileService {
    
    @Resource
    private StudentMapper studentMapper;
    
    @Override
    public StudentProfileVO getStudentProfile(Integer userId) {
        // 使用StudentMapper查询学生个人信息
        return studentMapper.selectStudentProfileById(userId);
    }
    
    @Override
    public boolean updateStudentProfile(Integer userId, UpdateStudentProfileRequestVO requestVO) {
        try {
            // 将Date类型转换为YYYY-MM-DD格式的字符串
            String birthDate = null;
            if (requestVO.getBirthDate() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                birthDate = sdf.format(requestVO.getBirthDate());
            }
            
            // 调用StudentMapper更新学生个人信息
            int result = studentMapper.updateStudentProfile(
                userId,
                requestVO.getUsername(),
                requestVO.getEmail(),
                requestVO.getPhone(),
                requestVO.getGender(),
                birthDate,
                requestVO.getEmergencyContact(),
                requestVO.getEmergencyPhone()
            );
            
            // 如果更新成功，返回true
            return result > 0;
        } catch (Exception e) {
            System.err.println("更新学生个人信息失败: userId=" + userId + ", error=" + e.getMessage());
            return false;
        }
    }
    

}