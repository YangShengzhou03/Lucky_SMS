package com.yangshengzhou.lucky_sms.vo.admin;

import java.util.List;

public class AnnouncementManagementVO {
    private List<AnnouncementItem> list;
    private Integer total;
    private Integer page;
    private Integer size;

    public List<AnnouncementItem> getList() {
        return list;
    }

    public void setList(List<AnnouncementItem> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public static class AnnouncementItem {
        private Integer announcementId;
        private String title;
        private String content;
        private String publisherName;
        private String departmentName;
        private String announcementTypeName;
        private Integer priority;
        private String targetAudience;
        private String statusName;
        private java.time.LocalDateTime publishTime;
        private java.time.LocalDateTime createTime;

        public Integer getAnnouncementId() {
            return announcementId;
        }

        public void setAnnouncementId(Integer announcementId) {
            this.announcementId = announcementId;
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

        public String getPublisherName() {
            return publisherName;
        }

        public void setPublisherName(String publisherName) {
            this.publisherName = publisherName;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getAnnouncementTypeName() {
            return announcementTypeName;
        }

        public void setAnnouncementTypeName(String announcementTypeName) {
            this.announcementTypeName = announcementTypeName;
        }

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public String getTargetAudience() {
            return targetAudience;
        }

        public void setTargetAudience(String targetAudience) {
            this.targetAudience = targetAudience;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public java.time.LocalDateTime getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(java.time.LocalDateTime publishTime) {
            this.publishTime = publishTime;
        }

        public java.time.LocalDateTime getCreateTime() {
            return createTime;
        }

        public void setCreateTime(java.time.LocalDateTime createTime) {
            this.createTime = createTime;
        }
    }
}
