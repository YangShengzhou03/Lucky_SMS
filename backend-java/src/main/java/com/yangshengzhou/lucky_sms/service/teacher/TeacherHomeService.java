package com.yangshengzhou.lucky_sms.service.teacher;

import com.yangshengzhou.lucky_sms.vo.teacher.HomeVO;

/**
 * 教师首页服务接口
 */
public interface TeacherHomeService {
    
    /**
     * 获取教师首页数据
     * @param userId 用户ID
     * @return 首页数据
     */
    HomeVO getHomeData(Integer userId);
}
