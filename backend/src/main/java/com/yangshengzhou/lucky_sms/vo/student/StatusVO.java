package com.yangshengzhou.lucky_sms.vo.student;

import com.yangshengzhou.lucky_sms.pojo.AcademicHistory;
import com.yangshengzhou.lucky_sms.pojo.BasicInfo;

import java.util.List;

public class StatusVO {

    private BasicInfo basicInfo;
    private List<AcademicHistory> academicHistory;

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public List<AcademicHistory> getAcademicHistory() {
        return academicHistory;
    }

    public void setAcademicHistory(List<AcademicHistory> academicHistory) {
        this.academicHistory = academicHistory;
    }
}