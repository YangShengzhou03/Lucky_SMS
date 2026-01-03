package com.yangshengzhou.lucky_sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yangshengzhou.lucky_sms.pojo.ReviewStatus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审核状态Mapper接口
 */
@Mapper
public interface ReviewStatusMapper extends BaseMapper<ReviewStatus> {
}