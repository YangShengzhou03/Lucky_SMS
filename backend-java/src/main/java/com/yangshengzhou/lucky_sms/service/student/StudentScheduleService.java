package com.yangshengzhou.lucky_sms.service.student;

import com.yangshengzhou.lucky_sms.vo.teacher.ScheduleVO;

public interface StudentScheduleService {
    
    ScheduleVO getSchedule(String semester, Integer studentId);
}
