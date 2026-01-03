package com.yangshengzhou.lucky_sms.service.student.impl;

import com.yangshengzhou.lucky_sms.mapper.student.StudentViewMapper;
import com.yangshengzhou.lucky_sms.service.student.GradeService;
import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class GradeServiceImpl implements GradeService {

    @Resource
    private StudentViewMapper studentViewMapper;

    public GradesVO getGradesData(Integer userId) {
        GradesVO gradesVO = studentViewMapper.getGradesData(userId);
        if (gradesVO == null) {
            gradesVO = new GradesVO();
        }
        return gradesVO;
    }

    public GradesVO getGradesDataBySemester(Integer userId, String semester) {
        GradesVO gradesVO = studentViewMapper.getGradesDataBySemester(userId, semester);
        if (gradesVO == null) {
            gradesVO = new GradesVO();
        }
        return gradesVO;
    }

    public GradesVO getGradesDataWithPagination(Integer userId, Integer page, Integer size) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("page", page);
        params.put("size", size);
        params.put("offset", (page - 1) * size);
        GradesVO gradesVO = studentViewMapper.getGradesDataWithPagination(params);
        if (gradesVO == null) {
            gradesVO = new GradesVO();
            gradesVO.setPage(page);
            gradesVO.setSize(size);
            gradesVO.setTotal(0);
        }
        return gradesVO;
    }
    
    public GradesVO getGradesDataBySemesterWithPagination(Integer userId, String semester, Integer page, Integer size) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("semester", semester);
        params.put("page", page);
        params.put("size", size);
        params.put("offset", (page - 1) * size);
        GradesVO gradesVO = studentViewMapper.getGradesDataBySemesterWithPagination(params);
        if (gradesVO == null) {
            gradesVO = new GradesVO();
            gradesVO.setPage(page);
            gradesVO.setSize(size);
            gradesVO.setTotal(0);
        }
        return gradesVO;
    }
}