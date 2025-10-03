package com.yangshengzhou.lucky_sms.vo.student;

import java.util.List;

// 返回给student端Home页面数据的封装，HomeVO包含一个map和列表。
public class HomeVO {
    private StudentVO student;
    private List<Announcement> announcements;

    public StudentVO getStudent() {
        return student;
    }

    public void setStudent(StudentVO student) {
        this.student = student;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }
}

