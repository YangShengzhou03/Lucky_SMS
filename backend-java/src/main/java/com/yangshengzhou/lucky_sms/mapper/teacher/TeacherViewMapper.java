package com.yangshengzhou.lucky_sms.mapper.teacher;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.vo.teacher.CourseManagementVO;
import com.yangshengzhou.lucky_sms.vo.teacher.GradeManagementVO;
import com.yangshengzhou.lucky_sms.vo.teacher.ScheduleVO;
import com.yangshengzhou.lucky_sms.vo.teacher.StudentManagementVO;
import com.yangshengzhou.lucky_sms.vo.teacher.TeacherProfileVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface TeacherViewMapper extends BaseMapper {

    TeacherProfileVO selectTeacherProfileById(Integer userId);

    int updateTeacherProfile(@Param("userId") Integer userId,
                            @Param("email") String email,
                            @Param("phone") String phone,
                            @Param("educationBackground") String educationBackground);

    IPage<StudentManagementVO.StudentInfo> getStudentList(Page<StudentManagementVO.StudentInfo> page,
                                                          @Param("teacherId") Integer teacherId,
                                                          @Param("keyword") String keyword);

    IPage<CourseManagementVO.CourseInfo> getCourseList(Page<CourseManagementVO.CourseInfo> page,
                                                      @Param("teacherId") Integer teacherId,
                                                      @Param("semester") String semester);

    GradeManagementVO getCourseGrades(@Param("courseId") Integer courseId,
                                     @Param("teacherId") Integer teacherId);

    int updateGrade(@Param("gradeId") Integer gradeId,
                   @Param("usualScore") Double usualScore,
                   @Param("midtermScore") Double midtermScore,
                   @Param("finalScore") Double finalScore);

    ScheduleVO getSchedule(@Param("teacherId") Integer teacherId,
                          @Param("semester") String semester);

    Integer getTotalStudents(@Param("teacherId") Integer teacherId);

    Integer getTotalCourses(@Param("teacherId") Integer teacherId);

    Integer getThisSemesterCourses(@Param("teacherId") Integer teacherId,
                                  @Param("semester") String semester);
}
