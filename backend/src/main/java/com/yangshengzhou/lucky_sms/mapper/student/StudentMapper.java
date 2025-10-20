package com.yangshengzhou.lucky_sms.mapper.student;

import com.yangshengzhou.lucky_sms.pojo.AcademicHistory;
import com.yangshengzhou.lucky_sms.pojo.Announcement;
import com.yangshengzhou.lucky_sms.pojo.BasicInfo;
import com.yangshengzhou.lucky_sms.pojo.Todos;
import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
import com.yangshengzhou.lucky_sms.vo.student.StudentProfileVO;
import com.yangshengzhou.lucky_sms.vo.student.StudentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    // 1. 查询学生信息
    StudentVO selectStudentById(Integer userId);

    // 2. 查询待办事项列表
    List<Todos> selectTodosList(Integer userId);

    // 3. 查询公告列表
    List<Announcement> selectAnnouncementList();

    // 4. 查询学生基本信息
    BasicInfo selectBasicInfoById(Integer userId);

    // 5. 查询学生成绩数据
    GradesVO getGradesData(Integer userId);

    // 6. 查询指定学期的学生成绩数据
    GradesVO getGradesDataBySemester(@Param("userId") Integer userId, @Param("semester") String semester);

    // 7. 查询分页的学生成绩数据
    GradesVO getGradesDataWithPagination(Map<String, Object> params);

    // 8. 查询指定学期的分页学生成绩数据
    GradesVO getGradesDataBySemesterWithPagination(Map<String, Object> params);

    // 9. 查询学生最大学分
    Integer getMaxCredits(Integer userId);

    // 10. 查询学生个人信息
    StudentProfileVO selectStudentProfileById(Integer userId);

    // 11. 更新学生个人信息
    int updateStudentProfile(@Param("userId") Integer userId,
                             @Param("username") String username,
                           @Param("email") String email,
                           @Param("phone") String phone,
                           @Param("gender") String gender,
                           @Param("birthDate") String birthDate,
                           @Param("emergencyContact") String emergencyContact,
                           @Param("emergencyPhone") String emergencyPhone);

//    List<AcademicHistory> selectAcademicHistoryList(Integer userId);
}
