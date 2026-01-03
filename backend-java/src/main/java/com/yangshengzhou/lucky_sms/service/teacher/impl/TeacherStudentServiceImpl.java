package com.yangshengzhou.lucky_sms.service.teacher.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.mapper.teacher.TeacherViewMapper;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherStudentService;
import com.yangshengzhou.lucky_sms.vo.teacher.StudentManagementVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class TeacherStudentServiceImpl implements TeacherStudentService {

    @Resource
    private TeacherViewMapper teacherViewMapper;

    @Override
    public StudentManagementVO getStudentList(Integer page, Integer size, String keyword, Integer teacherId) {
        StudentManagementVO vo = new StudentManagementVO();

        Page<StudentManagementVO.StudentInfo> pageParam = new Page<>(page, size);
        IPage<StudentManagementVO.StudentInfo> studentPage = teacherViewMapper.getStudentList(pageParam, teacherId, keyword);

        vo.setRecords(studentPage.getRecords());
        vo.setTotal(studentPage.getTotal());
        vo.setSize(size);
        vo.setCurrent(page);
        vo.setPages((int) studentPage.getPages());

        return vo;
    }
}
