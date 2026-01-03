package com.yangshengzhou.lucky_sms.service.teacher;

import com.yangshengzhou.lucky_sms.vo.teacher.TeacherProfileVO;

/**
 * 教师个人信息服务接口
 */
public interface TeacherProfileService {
    
    /**
     * 获取教师个人信息
     * @param userId 用户ID
     * @return 教师个人信息
     */
    TeacherProfileVO getTeacherProfile(Integer userId);
    
    /**
     * 更新教师个人信息
     * @param userId 用户ID
     * @param profile 个人信息
     * @return 是否成功
     */
    boolean updateTeacherProfile(Integer userId, TeacherProfileVO profile);
}
