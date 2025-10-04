package com.yangshengzhou.lucky_sms.service.student;

import com.yangshengzhou.lucky_sms.vo.student.GradesVO;

public interface GradeService {
    GradesVO getGradesData(Integer userId);
    GradesVO getGradesDataBySemester(Integer userId, String semester);
}