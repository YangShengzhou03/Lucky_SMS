package com.yangshengzhou.lucky_sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangshengzhou.lucky_sms.pojo.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
    IPage<Map<String, Object>> getAnnouncementList(Page<Map<String, Object>> page, @Param("ew") QueryWrapper<Map<String, Object>> queryWrapper);
}
