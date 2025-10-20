package com.yangshengzhou.lucky_sms.pojo;

public class Announcement {
    private String id;
    private String title;
    private String content;
    private String publish_date;
    private String department_name;
    private String announcement_type;
    private String priority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getAnnouncement_type() {
        return announcement_type;
    }

    public void setAnnouncement_type(String announcement_type) {
        this.announcement_type = announcement_type;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
