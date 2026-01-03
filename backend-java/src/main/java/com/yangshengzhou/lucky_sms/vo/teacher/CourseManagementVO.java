package com.yangshengzhou.lucky_sms.vo.teacher;

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
        private Integer credits;
        private String classTime;
        private String location;
        private Integer studentCount;

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

        public Integer getCredits() {
            return credits;
        }

        public void setCredits(Integer credits) {
            this.credits = credits;
        }

        public String getClassTime() {
            return classTime;
        }

        public void setClassTime(String classTime) {
            this.classTime = classTime;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Integer getStudentCount() {
            return studentCount;
        }

        public void setStudentCount(Integer studentCount) {
            this.studentCount = studentCount;
        }
    }
}
