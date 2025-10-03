package com.yangshengzhou.lucky_sms.vo.student;

import java.util.HashMap;
import java.util.List;

public class StudentVO {
    private String name;
    private String id;
    private String className;
    private String gpa;
    private String rank;
    private String classSize;
    private String courseCount;
    private HashMap<String, Object> nextCourse;
    public List<HashMap<String, Object>> todos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getClassSize() {
        return classSize;
    }

    public void setClassSize(String classSize) {
        this.classSize = classSize;
    }

    public String getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(String courseCount) {
        this.courseCount = courseCount;
    }

    public HashMap<String, Object> getNextCourse() {
        return nextCourse;
    }

    public void setNextCourse(HashMap<String, Object> nextCourse) {
        this.nextCourse = nextCourse;
    }

    public List<HashMap<String, Object>> getTodos() {
        return todos;
    }

    public void setTodos(List<HashMap<String, Object>> todos) {
        this.todos = todos;
    }
}
