package com.yangshengzhou.lucky_sms.service.student.impl;

import com.yangshengzhou.lucky_sms.mapper.student.StudentViewMapper;
import com.yangshengzhou.lucky_sms.pojo.Announcement;
import com.yangshengzhou.lucky_sms.service.student.HomeService;
import com.yangshengzhou.lucky_sms.vo.student.HomeVO;
import com.yangshengzhou.lucky_sms.vo.student.StudentVO;
import com.yangshengzhou.lucky_sms.pojo.Todos;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    private StudentViewMapper studentViewMapper;

    public HomeVO getHomeDate(Integer userid) {

        HomeVO homeVO = new HomeVO();

        StudentVO studentVO = studentViewMapper.selectStudentById(userid);
        List<Announcement> announcement = studentViewMapper.selectAnnouncementList();
        List<Todos> todos = studentViewMapper.selectTodosList(userid);

        if (studentVO == null) {
            throw new RuntimeException("获取用户数据失败");
        }

        homeVO.setStudent(studentVO);
        studentVO.setTodos(todos);
        homeVO.setAnnouncements(announcement);

        return homeVO;
    }
}
