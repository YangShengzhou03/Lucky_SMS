package com.yangshengzhou.lucky_sms.vo.student;

import java.time.LocalDateTime;

public class TimeConflictVO {
    
    private Boolean hasConflict;
    private String conflictMessage;
    private Integer conflictingCourseId;
    private String conflictingCourseName;
    private String conflictingCourseTime;
    private LocalDateTime conflictingStartTime;
    private LocalDateTime conflictingEndTime;
    private String suggestedSolution;
    
    public TimeConflictVO() {
        this.hasConflict = false;
    }
    
    public TimeConflictVO(Boolean hasConflict, String conflictMessage) {
        this.hasConflict = hasConflict;
        this.conflictMessage = conflictMessage;
    }

    public Boolean getHasConflict() {
        return hasConflict;
    }

    public void setHasConflict(Boolean hasConflict) {
        this.hasConflict = hasConflict;
    }

    public String getConflictMessage() {
        return conflictMessage;
    }

    public void setConflictMessage(String conflictMessage) {
        this.conflictMessage = conflictMessage;
    }

    public Integer getConflictingCourseId() {
        return conflictingCourseId;
    }

    public void setConflictingCourseId(Integer conflictingCourseId) {
        this.conflictingCourseId = conflictingCourseId;
    }

    public String getConflictingCourseName() {
        return conflictingCourseName;
    }

    public void setConflictingCourseName(String conflictingCourseName) {
        this.conflictingCourseName = conflictingCourseName;
    }

    public String getConflictingCourseTime() {
        return conflictingCourseTime;
    }

    public void setConflictingCourseTime(String conflictingCourseTime) {
        this.conflictingCourseTime = conflictingCourseTime;
    }

    public LocalDateTime getConflictingStartTime() {
        return conflictingStartTime;
    }

    public void setConflictingStartTime(LocalDateTime conflictingStartTime) {
        this.conflictingStartTime = conflictingStartTime;
    }

    public LocalDateTime getConflictingEndTime() {
        return conflictingEndTime;
    }

    public void setConflictingEndTime(LocalDateTime conflictingEndTime) {
        this.conflictingEndTime = conflictingEndTime;
    }

    public String getSuggestedSolution() {
        return suggestedSolution;
    }

    public void setSuggestedSolution(String suggestedSolution) {
        this.suggestedSolution = suggestedSolution;
    }
}