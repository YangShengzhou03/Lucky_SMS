package com.yangshengzhou.lucky_sms.service.student;

import com.yangshengzhou.lucky_sms.pojo.Announcement;
import com.yangshengzhou.lucky_sms.vo.student.HomeVO;

import java.util.List;

public interface HomeService {

    HomeVO getHomeDate(Integer userid);

    List<Announcement> getAnnouncements(Integer userId);
}
