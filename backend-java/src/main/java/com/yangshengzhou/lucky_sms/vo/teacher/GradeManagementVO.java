package com.yangshengzhou.lucky_sms.vo.teacher;

import java.util.List;

public class GradeManagementVO {
    private List<GradeInfo> records;

    public List<GradeInfo> getRecords() {
        return records;
    }

    public void setRecords(List<GradeInfo> records) {
        this.records = records;
    }

    public static class GradeInfo {
        private Integer gradeId;
        private String studentId;
        private String studentName;
        private Double attendance;
        private Double midterm;
        private Double finalScore;
        private Double total;
        private String letterGrade;

        public Integer getGradeId() {
            return gradeId;
        }

        public void setGradeId(Integer gradeId) {
            this.gradeId = gradeId;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public Double getAttendance() {
            return attendance;
        }

        public void setAttendance(Double attendance) {
            this.attendance = attendance;
        }

        public Double getMidterm() {
            return midterm;
        }

        public void setMidterm(Double midterm) {
            this.midterm = midterm;
        }

        public Double getFinalScore() {
            return finalScore;
        }

        public void setFinalScore(Double finalScore) {
            this.finalScore = finalScore;
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }

        public String getLetterGrade() {
            return letterGrade;
        }

        public void setLetterGrade(String letterGrade) {
            this.letterGrade = letterGrade;
        }
    }
}
