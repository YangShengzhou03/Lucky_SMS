package com.yangshengzhou.lucky_sms.vo.student;

import com.yangshengzhou.lucky_sms.pojo.Todos;

import java.util.HashMap;
import java.util.List;

public class StudentVO {
    private String username;
    private String student_no;
    private String class_name;
    private String gpa;
    private String class_rank;
    private String classSize;
    private String course_count;
    private HashMap<String, Object> nextCourse;
    public List<Todos> todos;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStudent_no() {
        return student_no;
    }

    public void setStudent_no(String student_no) {
        this.student_no = student_no;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getClass_rank() {
        return class_rank;
    }

    public void setClass_rank(String class_rank) {
        this.class_rank = class_rank;
    }

    public String getClassSize() {
        return classSize;
    }

    public void setClassSize(String classSize) {
        this.classSize = classSize;
    }

    public String getCourse_count() {
        return course_count;
    }

    public void setCourse_count(String course_count) {
        this.course_count = course_count;
    }

    public HashMap<String, Object> getNextCourse() {
        return nextCourse;
    }

    public void setNextCourse(HashMap<String, Object> nextCourse) {
        this.nextCourse = nextCourse;
    }

    public List<Todos> getTodos() {
        return todos;
    }

    public void setTodos(List<Todos> todos) {
        this.todos = todos;
    }
}
