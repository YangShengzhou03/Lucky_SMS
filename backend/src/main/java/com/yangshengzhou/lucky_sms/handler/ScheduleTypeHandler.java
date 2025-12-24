package com.yangshengzhou.lucky_sms.handler;

import com.yangshengzhou.lucky_sms.vo.student.CourseSelectionVO;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@MappedTypes(List.class)
public class ScheduleTypeHandler extends BaseTypeHandler<List<CourseSelectionVO.ScheduleVO>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<CourseSelectionVO.ScheduleVO> parameter, JdbcType jdbcType) throws SQLException {
    }

    @Override
    public List<CourseSelectionVO.ScheduleVO> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseSchedule(rs.getString(columnName));
    }

    @Override
    public List<CourseSelectionVO.ScheduleVO> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseSchedule(rs.getString(columnIndex));
    }

    @Override
    public List<CourseSelectionVO.ScheduleVO> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseSchedule(cs.getString(columnIndex));
    }

    private List<CourseSelectionVO.ScheduleVO> parseSchedule(String scheduleStr) {
        List<CourseSelectionVO.ScheduleVO> scheduleList = new ArrayList<>();
        
        if (scheduleStr == null || scheduleStr.trim().isEmpty()) {
            return scheduleList;
        }
        
        String[] parts = scheduleStr.split(" ");
        if (parts.length >= 2) {
            CourseSelectionVO.ScheduleVO scheduleVO = new CourseSelectionVO.ScheduleVO();
            
            String dayStr = parts[0];
            int day = parseDay(dayStr);
            scheduleVO.setDay(day);
            
            String timeSlot = parts[1];
            int timeSlotNum = parseTimeSlot(timeSlot);
            scheduleVO.setTimeSlot(timeSlotNum);
            
            scheduleList.add(scheduleVO);
        }
        
        return scheduleList;
    }
    
    private int parseDay(String dayStr) {
        if (dayStr.contains("周一")) return 1;
        if (dayStr.contains("周二")) return 2;
        if (dayStr.contains("周三")) return 3;
        if (dayStr.contains("周四")) return 4;
        if (dayStr.contains("周五")) return 5;
        if (dayStr.contains("周六")) return 6;
        if (dayStr.contains("周日")) return 7;
        return 1;
    }
    
    private int parseTimeSlot(String timeSlot) {
        if (timeSlot.contains("8:00-9:40")) return 1;
        if (timeSlot.contains("10:00-11:40")) return 2;
        if (timeSlot.contains("14:00-15:40")) return 3;
        if (timeSlot.contains("16:00-17:40")) return 4;
        if (timeSlot.contains("19:00-20:40")) return 5;
        return 1;
    }
}