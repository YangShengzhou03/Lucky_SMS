package com.yangshengzhou.lucky_sms.vo.student;

import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}