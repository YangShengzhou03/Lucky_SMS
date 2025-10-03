package com.yangshengzhou.lucky_sms.pojo;

public class BasicInfo {
    private String status;
    private String gpa;
    private String totalCredits;
    private String completedCredits;
    public String enrollmentDate;
    public String expectedGraduationDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(String totalCredits) {
        this.totalCredits = totalCredits;
    }

    public String getCompletedCredits() {
        return completedCredits;
    }

    public void setCompletedCredits(String completedCredits) {
        this.completedCredits = completedCredits;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getExpectedGraduationDate() {
        return expectedGraduationDate;
    }

    public void setExpectedGraduationDate(String expectedGraduationDate) {
        this.expectedGraduationDate = expectedGraduationDate;
    }
}
