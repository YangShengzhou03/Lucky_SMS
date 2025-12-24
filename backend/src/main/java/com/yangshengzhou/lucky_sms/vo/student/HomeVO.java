package com.yangshengzhou.lucky_sms.vo.student;

import com.yangshengzhou.lucky_sms.pojo.Announcement;

import java.util.List;

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

