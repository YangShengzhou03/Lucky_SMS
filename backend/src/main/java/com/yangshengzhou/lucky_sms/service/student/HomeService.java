package com.yangshengzhou.lucky_sms.service.student;

import com.yangshengzhou.lucky_sms.vo.student.Announcement;
import com.yangshengzhou.lucky_sms.vo.student.HomeVO;
import com.yangshengzhou.lucky_sms.vo.student.StudentVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class HomeService {

    public HomeVO getHomeDate(Integer userid) {

        HomeVO homeVO = new HomeVO();

        StudentVO studentVO = new StudentVO();
        List<Announcement> announcement = new ArrayList<Announcement>();

        /*
          模拟装填数据
         */
        studentVO.setName("李四");
        studentVO.setId("20221944");
        studentVO.setClassName("信管");
        studentVO.setGpa("3.99");
        studentVO.setRank("222");
        studentVO.setClassSize("23");
        studentVO.setCourseCount("2");
        HashMap<String, Object> course = new HashMap<>();
        course.put("name", "数学课");
        studentVO.setNextCourse(course);

        homeVO.setStudent(studentVO);
        homeVO.setAnnouncements(announcement);

        return homeVO;
    }
}
