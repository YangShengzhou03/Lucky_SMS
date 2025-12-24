package com.yangshengzhou.lucky_sms.mapper.student;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangshengzhou.lucky_sms.pojo.AcademicHistory;
import com.yangshengzhou.lucky_sms.pojo.Announcement;
import com.yangshengzhou.lucky_sms.pojo.BasicInfo;
import com.yangshengzhou.lucky_sms.pojo.Todos;
import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
import com.yangshengzhou.lucky_sms.vo.student.StudentProfileVO;
import com.yangshengzhou.lucky_sms.vo.student.StudentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StudentViewMapper extends BaseMapper<StudentVO> {
    StudentVO selectStudentById(Integer userId);

    List<Todos> selectTodosList(Integer userId);

    List<Announcement> selectAnnouncementList();

    BasicInfo selectBasicInfoById(Integer userId);

    GradesVO getGradesData(Integer userId);

    GradesVO getGradesDataBySemester(@Param("userId") Integer userId, @Param("semester") String semester);

    GradesVO getGradesDataWithPagination(Map<String, Object> params);

    GradesVO getGradesDataBySemesterWithPagination(Map<String, Object> params);

    Integer getMaxCredits(Integer userId);

    StudentProfileVO selectStudentProfileById(Integer userId);

    int updateStudentProfile(@Param("userId") Integer userId,
                             @Param("username") String username,
                           @Param("email") String email,
                           @Param("phone") String phone,
                           @Param("gender") String gender,
                           @Param("birthDate") String birthDate,
                           @Param("emergencyContact") String emergencyContact,
                           @Param("emergencyPhone") String emergencyPhone);
}
