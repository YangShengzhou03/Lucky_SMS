package com.yangshengzhou.lucky_sms.service.student;

import com.yangshengzhou.lucky_sms.vo.student.StudentProfileVO;
import com.yangshengzhou.lucky_sms.vo.student.UpdateStudentProfileRequestVO;

/**
 * 学生个人信息服务接口
 */
public interface ProfileService {
    
    /**
     * 获取学生个人信息
     * @param userId 用户ID
     * @return 学生个人信息VO
     */
    StudentProfileVO getStudentProfile(Integer userId);
    
    /**
     * 更新学生个人信息
     * @param userId 用户ID
     * @param requestVO 更新请求VO
     * @return 是否更新成功
     */
    boolean updateStudentProfile(Integer userId, UpdateStudentProfileRequestVO requestVO);
}