package com.yangshengzhou.lucky_sms.service.teacher;

import com.yangshengzhou.lucky_sms.vo.teacher.ScheduleVO;

/**
 * 教师课表服务接口
 */
public interface TeacherScheduleService {
    
    /**
     * 获取教师课表
     * @param semester 学期
     * @param teacherId 教师ID
     * @return 课表数据
     */
    ScheduleVO getSchedule(String semester, Integer teacherId);
}
