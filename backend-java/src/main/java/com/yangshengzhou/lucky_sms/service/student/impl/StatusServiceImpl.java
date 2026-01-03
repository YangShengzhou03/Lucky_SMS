package com.yangshengzhou.lucky_sms.service.student.impl;

import com.yangshengzhou.lucky_sms.mapper.student.StudentViewMapper;
import com.yangshengzhou.lucky_sms.pojo.BasicInfo;
import com.yangshengzhou.lucky_sms.service.student.StatusService;
import com.yangshengzhou.lucky_sms.vo.student.StatusVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class StatusServiceImpl implements StatusService {
    @Resource
    private StudentViewMapper studentViewMapper;

    public StatusVO getStatusDate(Integer userId) {
        StatusVO statusVO = new StatusVO();
        BasicInfo basicInfo = studentViewMapper.selectBasicInfoById(userId);

        statusVO.setBasicInfo(basicInfo);

        return statusVO;
    }
}
