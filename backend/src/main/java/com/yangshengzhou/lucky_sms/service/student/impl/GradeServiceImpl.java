package com.yangshengzhou.lucky_sms.service.student.impl;

import com.yangshengzhou.lucky_sms.mapper.student.StudentMapper;
import com.yangshengzhou.lucky_sms.service.student.GradeService;
import com.yangshengzhou.lucky_sms.vo.student.GradesVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class GradeServiceImpl implements GradeService {

    @Resource
    private StudentMapper studentMapper;

    public GradesVO getGradesData(Integer userId) {
        // 调用Mapper从数据库查询成绩数据
        GradesVO gradesVO = studentMapper.getGradesData(userId);
        // 如果查询结果为null，返回一个初始化的GradesVO对象
        if (gradesVO == null) {
            gradesVO = new GradesVO();
        }
        return gradesVO;
    }

    public GradesVO getGradesDataBySemester(Integer userId, String semester) {
        // 调用Mapper从数据库查询指定学期的成绩数据
        GradesVO gradesVO = studentMapper.getGradesDataBySemester(userId, semester);
        // 如果查询结果为null，返回一个初始化的GradesVO对象
        if (gradesVO == null) {
            gradesVO = new GradesVO();
        }
        return gradesVO;
    }

    public GradesVO getGradesDataWithPagination(Integer userId, Integer page, Integer size) {
        // 调用Mapper从数据库查询分页成绩数据
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("page", page);
        params.put("size", size);
        params.put("offset", (page - 1) * size);
        GradesVO gradesVO = studentMapper.getGradesDataWithPagination(params);
        // 如果查询结果为null，返回一个初始化的GradesVO对象
        if (gradesVO == null) {
            gradesVO = new GradesVO();
            gradesVO.setPage(page);
            gradesVO.setSize(size);
            gradesVO.setTotal(0);
        }
        return gradesVO;
    }
    
    public GradesVO getGradesDataBySemesterWithPagination(Integer userId, String semester, Integer page, Integer size) {
        // 调用Mapper从数据库查询指定学期的分页成绩数据
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("semester", semester);
        params.put("page", page);
        params.put("size", size);
        params.put("offset", (page - 1) * size);
        GradesVO gradesVO = studentMapper.getGradesDataBySemesterWithPagination(params);
        // 如果查询结果为null，返回一个初始化的GradesVO对象
        if (gradesVO == null) {
            gradesVO = new GradesVO();
            gradesVO.setPage(page);
            gradesVO.setSize(size);
            gradesVO.setTotal(0);
        }
        return gradesVO;
    }
}