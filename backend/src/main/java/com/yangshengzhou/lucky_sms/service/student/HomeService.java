package com.yangshengzhou.lucky_sms.service.student;

import com.yangshengzhou.lucky_sms.mapper.student.StudentMapper;
import com.yangshengzhou.lucky_sms.vo.student.Announcement;
import com.yangshengzhou.lucky_sms.vo.student.HomeVO;
import com.yangshengzhou.lucky_sms.vo.student.StudentVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomeService {
    @Resource
    private StudentMapper studentMapper;

    public HomeVO getHomeDate(Integer userid) {

        HomeVO homeVO = new HomeVO();

        StudentVO studentVO = studentMapper.selectStudentById(userid);
        List<Announcement> announcement = new ArrayList<>();

        if (studentVO == null) {
            throw new RuntimeException("获取用户数据失败");
        }

        /*
          模拟装填数据，实际应用应该上面返回
         */

        homeVO.setStudent(studentVO);
        homeVO.setAnnouncements(announcement);

        return homeVO;
    }
}
