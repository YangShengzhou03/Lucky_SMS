package com.yangshengzhou.lucky_sms.vo.teacher;

import java.util.List;

public class ScheduleVO {
    private List<ClassInfo> classes;

    public List<ClassInfo> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassInfo> classes) {
        this.classes = classes;
    }

    public static class ClassInfo {
        private Integer dayOfWeek;
        private Integer section;
        private String courseName;
        private String location;
        private String className;
        private Integer studentCount;

        public Integer getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public Integer getSection() {
            return section;
        }

        public void setSection(Integer section) {
            this.section = section;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Integer getStudentCount() {
            return studentCount;
        }

        public void setStudentCount(Integer studentCount) {
            this.studentCount = studentCount;
        }
    }
}
