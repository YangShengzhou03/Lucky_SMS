package com.yangshengzhou.lucky_sms.vo.student;

import java.util.ArrayList;
import java.util.List;

public class GradesVO {
    private List<CourseGrade> courseGrades = new ArrayList<>();
    private StudentGradeStats gradeStats;
    private List<SemesterGPA> semesterGPAList = new ArrayList<>();
    private Integer total;
    private Integer page;
    private Integer size;

    public GradesVO() {
        this.courseGrades = new ArrayList<>();
        this.semesterGPAList = new ArrayList<>();
    }

    public List<CourseGrade> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(List<CourseGrade> courseGrades) {
        this.courseGrades = courseGrades;
    }

    public StudentGradeStats getGradeStats() {
        return gradeStats;
    }

    public void setGradeStats(StudentGradeStats gradeStats) {
        this.gradeStats = gradeStats;
    }

    public List<SemesterGPA> getSemesterGPAList() {
        return semesterGPAList;
    }

    public void setSemesterGPAList(List<SemesterGPA> semesterGPAList) {
        this.semesterGPAList = semesterGPAList;
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

    public static class CourseGrade {
        private Integer id;
        private String courseName;
        private String courseCode;
        private String courseType;
        private Integer credits;
        private Integer score;
        private Double gpa;
        private String semester;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCourseCode() {
            return courseCode;
        }

        public void setCourseCode(String courseCode) {
            this.courseCode = courseCode;
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

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }

        public Double getGpa() {
            return gpa;
        }

        public void setGpa(Double gpa) {
            this.gpa = gpa;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }
    }

    public static class StudentGradeStats {
        private Double gpa;
        private Integer avgScore;
        private Integer rank;
        private Integer classSize;
        private Integer completedCredits;
        private Integer totalCredits;

        public Double getGpa() {
            return gpa;
        }

        public void setGpa(Double gpa) {
            this.gpa = gpa;
        }

        public Integer getAvgScore() {
            return avgScore;
        }

        public void setAvgScore(Integer avgScore) {
            this.avgScore = avgScore;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public Integer getClassSize() {
            return classSize;
        }

        public void setClassSize(Integer classSize) {
            this.classSize = classSize;
        }

        public Integer getCompletedCredits() {
            return completedCredits;
        }

        public void setCompletedCredits(Integer completedCredits) {
            this.completedCredits = completedCredits;
        }

        public Integer getTotalCredits() {
            return totalCredits;
        }

        public void setTotalCredits(Integer totalCredits) {
            this.totalCredits = totalCredits;
        }
    }

    public static class SemesterGPA {
        private String semester;
        private Double gpa;
        private Integer avgScore;
        private Integer credits;

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }

        public Double getGpa() {
            return gpa;
        }

        public void setGpa(Double gpa) {
            this.gpa = gpa;
        }

        public Integer getAvgScore() {
            return avgScore;
        }

        public void setAvgScore(Integer avgScore) {
            this.avgScore = avgScore;
        }

        public Integer getCredits() {
            return credits;
        }

        public void setCredits(Integer credits) {
            this.credits = credits;
        }
    }
}