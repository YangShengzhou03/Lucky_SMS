package com.yangshengzhou.lucky_sms.service.student;

import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    @Override
    public GradesVO getGradesData(Integer userId) {
        // 在实际应用中，这里应该从数据库查询数据
        // 这里为了演示，我们使用模拟数据
        GradesVO gradesVO = new GradesVO();
        
        // 设置课程成绩数据
        List<GradesVO.CourseGrade> courseGrades = new ArrayList<>();
        
        GradesVO.CourseGrade grade1 = new GradesVO.CourseGrade();
        grade1.setId(1);
        grade1.setCourseName("数据结构与算法");
        grade1.setCourseCode("CS101");
        grade1.setCourseType("必修课");
        grade1.setCredits(4);
        grade1.setScore(85);
        grade1.setGpa(3.7);
        grade1.setSemester("2023-2024-2");
        courseGrades.add(grade1);
        
        GradesVO.CourseGrade grade2 = new GradesVO.CourseGrade();
        grade2.setId(2);
        grade2.setCourseName("计算机网络");
        grade2.setCourseCode("CS102");
        grade2.setCourseType("必修课");
        grade2.setCredits(3);
        grade2.setScore(78);
        grade2.setGpa(2.8);
        grade2.setSemester("2023-2024-2");
        courseGrades.add(grade2);
        
        GradesVO.CourseGrade grade3 = new GradesVO.CourseGrade();
        grade3.setId(3);
        grade3.setCourseName("操作系统");
        grade3.setCourseCode("CS103");
        grade3.setCourseType("必修课");
        grade3.setCredits(4);
        grade3.setScore(92);
        grade3.setGpa(4.0);
        grade3.setSemester("2023-2024-2");
        courseGrades.add(grade3);
        
        GradesVO.CourseGrade grade4 = new GradesVO.CourseGrade();
        grade4.setId(4);
        grade4.setCourseName("人工智能导论");
        grade4.setCourseCode("CS104");
        grade4.setCourseType("选修课");
        grade4.setCredits(3);
        grade4.setScore(88);
        grade4.setGpa(3.9);
        grade4.setSemester("2023-2024-2");
        courseGrades.add(grade4);
        
        GradesVO.CourseGrade grade5 = new GradesVO.CourseGrade();
        grade5.setId(5);
        grade5.setCourseName("高等数学（下）");
        grade5.setCourseCode("MATH102");
        grade5.setCourseType("必修课");
        grade5.setCredits(5);
        grade5.setScore(76);
        grade5.setGpa(2.6);
        grade5.setSemester("2023-2024-1");
        courseGrades.add(grade5);
        
        GradesVO.CourseGrade grade6 = new GradesVO.CourseGrade();
        grade6.setId(6);
        grade6.setCourseName("线性代数");
        grade6.setCourseCode("MATH103");
        grade6.setCourseType("必修课");
        grade6.setCredits(4);
        grade6.setScore(82);
        grade6.setGpa(3.3);
        grade6.setSemester("2023-2024-1");
        courseGrades.add(grade6);
        
        gradesVO.setCourseGrades(courseGrades);
        
        // 设置学生成绩统计数据
        GradesVO.StudentGradeStats gradeStats = new GradesVO.StudentGradeStats();
        gradeStats.setGpa(3.6);
        gradeStats.setAvgScore(86);
        gradeStats.setRank(12);
        gradeStats.setClassSize(45);
        gradeStats.setCompletedCredits(68);
        gradeStats.setTotalCredits(140);
        gradesVO.setGradeStats(gradeStats);
        
        // 设置学期GPA数据
        List<GradesVO.SemesterGPA> semesterGPAList = new ArrayList<>();
        
        GradesVO.SemesterGPA gpa1 = new GradesVO.SemesterGPA();
        gpa1.setSemester("2022-2023-1");
        gpa1.setGpa(3.2);
        gpa1.setAvgScore(80);
        gpa1.setCredits(28);
        semesterGPAList.add(gpa1);
        
        GradesVO.SemesterGPA gpa2 = new GradesVO.SemesterGPA();
        gpa2.setSemester("2022-2023-2");
        gpa2.setGpa(3.5);
        gpa2.setAvgScore(84);
        gpa2.setCredits(32);
        semesterGPAList.add(gpa2);
        
        GradesVO.SemesterGPA gpa3 = new GradesVO.SemesterGPA();
        gpa3.setSemester("2023-2024-1");
        gpa3.setGpa(3.4);
        gpa3.setAvgScore(82);
        gpa3.setCredits(30);
        semesterGPAList.add(gpa3);
        
        GradesVO.SemesterGPA gpa4 = new GradesVO.SemesterGPA();
        gpa4.setSemester("2023-2024-2");
        gpa4.setGpa(3.6);
        gpa4.setAvgScore(86);
        gpa4.setCredits(28);
        semesterGPAList.add(gpa4);
        
        gradesVO.setSemesterGPAList(semesterGPAList);
        
        return gradesVO;
    }

    @Override
    public GradesVO getGradesDataBySemester(Integer userId, String semester) {
        // 获取所有成绩数据
        GradesVO allGrades = getGradesData(userId);
        
        // 创建新的VO对象，只包含指定学期的数据
        GradesVO semesterGrades = new GradesVO();
        
        // 筛选指定学期的课程成绩
        List<GradesVO.CourseGrade> filteredCourseGrades = new ArrayList<>();
        for (GradesVO.CourseGrade grade : allGrades.getCourseGrades()) {
            if (semester.equals(grade.getSemester())) {
                filteredCourseGrades.add(grade);
            }
        }
        semesterGrades.setCourseGrades(filteredCourseGrades);
        
        // 计算当前学期的统计数据
        if (!filteredCourseGrades.isEmpty()) {
            GradesVO.StudentGradeStats semesterStats = new GradesVO.StudentGradeStats();
            
            // 计算平均GPA
            double totalGpa = 0;
            double totalCredits = 0;
            int totalScore = 0;
            for (GradesVO.CourseGrade grade : filteredCourseGrades) {
                totalGpa += grade.getGpa() * grade.getCredits();
                totalCredits += grade.getCredits();
                totalScore += grade.getScore();
            }
            
            semesterStats.setGpa(totalGpa / totalCredits);
            semesterStats.setAvgScore(totalScore / filteredCourseGrades.size());
            // 这里简化处理，实际应该从数据库获取排名和班级大小
            semesterStats.setRank(allGrades.getGradeStats().getRank());
            semesterStats.setClassSize(allGrades.getGradeStats().getClassSize());
            semesterStats.setCompletedCredits((int) totalCredits);
            semesterStats.setTotalCredits(allGrades.getGradeStats().getTotalCredits());
            
            semesterGrades.setGradeStats(semesterStats);
        }
        
        // 设置学期GPA数据
        semesterGrades.setSemesterGPAList(allGrades.getSemesterGPAList());
        
        return semesterGrades;
    }
}