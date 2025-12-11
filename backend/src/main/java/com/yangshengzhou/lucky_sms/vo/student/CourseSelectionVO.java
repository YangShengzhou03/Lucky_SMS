package com.yangshengzhou.lucky_sms.vo.student;


import java.util.List;

public class CourseSelectionVO {
    private Integer id;
    private String name;
    private String code;
    private String teacher;
    private String time;
    private String location;
    private Integer credits;
    private Integer capacityUsed;
    private String category;
    private List<ScheduleVO> schedule;
    private Integer maxStudents;
    private Integer currentStudents;
    private String semester;

    public static class ScheduleVO {
        private Integer day;
        private Integer timeSlot;

        public Integer getDay() {
            return day;
        }

        public void setDay(Integer day) {
            this.day = day;
        }

        public Integer getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(Integer timeSlot) {
            this.timeSlot = timeSlot;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getCapacityUsed() {
        return capacityUsed;
    }

    public void setCapacityUsed(Integer capacityUsed) {
        this.capacityUsed = capacityUsed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ScheduleVO> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleVO> schedule) {
        this.schedule = schedule;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Integer getCurrentStudents() {
        return currentStudents;
    }

    public void setCurrentStudents(Integer currentStudents) {
        this.currentStudents = currentStudents;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourseName() {
        return name;
    }

    public void setCourseName(String courseName) {
        this.name = courseName;
    }
}