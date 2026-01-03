package com.yangshengzhou.lucky_sms.service.teacher.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.mapper.teacher.TeacherViewMapper;
import com.yangshengzhou.lucky_sms.service.teacher.TeacherCourseService;
import com.yangshengzhou.lucky_sms.vo.teacher.CourseManagementVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class TeacherCourseServiceImpl implements TeacherCourseService {

    @Resource
    private TeacherViewMapper teacherViewMapper;

    @Override
    public CourseManagementVO getCourseList(Integer page, Integer size, String semester, Integer teacherId) {
        CourseManagementVO vo = new CourseManagementVO();

        Page<CourseManagementVO.CourseInfo> pageParam = new Page<>(page, size);
        IPage<CourseManagementVO.CourseInfo> coursePage = teacherViewMapper.getCourseList(pageParam, teacherId, semester);

        vo.setRecords(coursePage.getRecords());
        vo.setTotal(coursePage.getTotal());
        vo.setSize(size);
        vo.setCurrent(page);
        vo.setPages((int) coursePage.getPages());

        return vo;
    }
}
