package com.yangshengzhou.lucky_sms.mapper.student;

import com.yangshengzhou.lucky_sms.pojo.AcademicHistory;
import com.yangshengzhou.lucky_sms.pojo.Announcement;
import com.yangshengzhou.lucky_sms.pojo.BasicInfo;
import com.yangshengzhou.lucky_sms.pojo.Todos;
import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
import com.yangshengzhou.lucky_sms.vo.student.StudentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    // 根据userId返回student数据
    StudentVO selectStudentById(Integer userId);

    // 根据userId返回List<todos>数据
    List<Todos> selectTodosList(Integer userId);

    // 根据userId返回List<announcement>数据
    List<Announcement> selectAnnouncementList();

    // 根据userId返回basicInfo数据
    BasicInfo selectBasicInfoById(Integer userId);

    // 根据userId返回List<AcademicHistory>数据
    List<AcademicHistory> selectAcademicHistoryList(Integer userId);

    GradesVO getGradesData(Integer userId);
    
    GradesVO getGradesDataBySemester(Integer userId, String semester);

}
