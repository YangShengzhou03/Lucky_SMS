package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.*;

/**
 * 教学安排实体类
 */
@TableName("teaching_assignments")
public class TeachingAssignment extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Integer assignmentId;
    
    @TableField("course_id")
    private Integer courseId;
    
    @TableField("teacher_id")
    private Integer teacherId;
    
    @TableField("semester_id")
    private Integer semesterId;
    
    @TableField("class_time")
    private String classTime;
    
    private String classroom;
    
    @TableField("max_students")
    private Integer maxStudents;
    
    @TableField("current_students")
    private Integer currentStudents;
    
    @TableField("credit_hours")
    private Integer creditHours;
    
    @TableField("course_type_id")
    private Integer courseTypeId;
    
    @TableField("assessment_method_id")
    private Integer assessmentMethodId;
    
    @TableField("status_id")
    private Integer statusId;
    
    private String description;
    private String requirements;
    

    @TableField(exist = false)
    private Course course;
    
    @TableField(exist = false)
    private Teacher teacher;
    
    @TableField(exist = false)
    private Semester semester;
    
    @TableField(exist = false)
    private CourseType courseType;
    
    @TableField(exist = false)
    private AssessmentMethod assessmentMethod;
    
    @TableField(exist = false)
    private StatusType status;

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
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

    public Integer getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(Integer creditHours) {
        this.creditHours = creditHours;
    }

    public Integer getCourseTypeId() {
        return courseTypeId;
    }

    public void setCourseTypeId(Integer courseTypeId) {
        this.courseTypeId = courseTypeId;
    }

    public Integer getAssessmentMethodId() {
        return assessmentMethodId;
    }

    public void setAssessmentMethodId(Integer assessmentMethodId) {
        this.assessmentMethodId = assessmentMethodId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public AssessmentMethod getAssessmentMethod() {
        return assessmentMethod;
    }

    public void setAssessmentMethod(AssessmentMethod assessmentMethod) {
        this.assessmentMethod = assessmentMethod;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}