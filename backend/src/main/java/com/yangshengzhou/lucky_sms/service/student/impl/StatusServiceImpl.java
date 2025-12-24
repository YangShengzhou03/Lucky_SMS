package com.yangshengzhou.lucky_sms.service.student.impl;

import com.yangshengzhou.lucky_sms.mapper.student.StudentMapper;
import com.yangshengzhou.lucky_sms.pojo.BasicInfo;
import com.yangshengzhou.lucky_sms.service.student.StatusService;
import com.yangshengzhou.lucky_sms.vo.student.StatusVO;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class StatusServiceImpl implements StatusService {
    @Resource
    private StudentMapper studentMapper;

    public StatusVO getStatusDate(Integer userId) {
        StatusVO statusVO = new StatusVO();
        BasicInfo basicInfo = studentMapper.selectBasicInfoById(userId);

        statusVO.setBasicInfo(basicInfo);

        return statusVO;
    }
}
