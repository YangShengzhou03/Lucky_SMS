package com.yangshengzhou.lucky_sms.vo.admin;

import java.util.List;

public class CourseManagementVO {
    private List<CourseInfo> records;
    private Long total;
    private Integer size;
    private Integer current;
    private Integer pages;

    public List<CourseInfo> getRecords() {
        return records;
    }

    public void setRecords(List<CourseInfo> records) {
        this.records = records;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public static class CourseInfo {
        private Integer id;
        private String courseCode;
        private String courseName;
        private String courseType;
        private String department;
        private String teacherName;
        private Integer credits;
        private Integer hours;
        private String semester;
        private Integer enrolledCount;
        private Integer maxCapacity;
        private String status;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCourseCode() {
            return courseCode;
        }

        public void setCourseCode(String courseCode) {
            this.courseCode = courseCode;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCourseType() {
            return courseType;
        }

        public void setCourseType(String courseType) {
            this.courseType = courseType;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public Integer getCredits() {
            return credits;
        }

        public void setCredits(Integer credits) {
            this.credits = credits;
        }

        public Integer getHours() {
            return hours;
        }

        public void setHours(Integer hours) {
            this.hours = hours;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }

        public Integer getEnrolledCount() {
            return enrolledCount;
        }

        public void setEnrolledCount(Integer enrolledCount) {
            this.enrolledCount = enrolledCount;
        }

        public Integer getMaxCapacity() {
            return maxCapacity;
        }

        public void setMaxCapacity(Integer maxCapacity) {
            this.maxCapacity = maxCapacity;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}