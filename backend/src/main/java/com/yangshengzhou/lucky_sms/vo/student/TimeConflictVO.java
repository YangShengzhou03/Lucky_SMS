package com.yangshengzhou.lucky_sms.vo.student;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 时间冲突信息VO
 */
@Data
public class TimeConflictVO {
    
    private Boolean hasConflict; // 是否有冲突
    private String conflictMessage; // 冲突描述
    private Integer conflictingCourseId; // 冲突课程ID
    private String conflictingCourseName; // 冲突课程名称
    private String conflictingCourseTime; // 冲突课程时间
    private LocalDateTime conflictingStartTime; // 冲突开始时间
    private LocalDateTime conflictingEndTime; // 冲突结束时间
    private String suggestedSolution; // 建议解决方案
    
    public TimeConflictVO() {
        this.hasConflict = false;
    }
    
    public TimeConflictVO(Boolean hasConflict, String conflictMessage) {
        this.hasConflict = hasConflict;
        this.conflictMessage = conflictMessage;
    }
}