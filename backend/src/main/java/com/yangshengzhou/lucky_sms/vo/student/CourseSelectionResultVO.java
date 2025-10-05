package com.yangshengzhou.lucky_sms.vo.student;

public class CourseSelectionResultVO {
    private Boolean success;
    private String message;
    private CourseSelectionVO course;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CourseSelectionVO getCourse() {
        return course;
    }

    public void setCourse(CourseSelectionVO course) {
        this.course = course;
    }
}