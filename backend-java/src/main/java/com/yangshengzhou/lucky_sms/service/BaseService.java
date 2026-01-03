package com.yangshengzhou.lucky_sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BaseService<T> extends IService<T> {
    
    Page<T> findPage(int page, int size);
    
    Page<T> findPageWithQuery(int page, int size, Object query);
    
    boolean deleteBatch(List<Integer> ids);
    
    boolean updateStatusBatch(List<Integer> ids, Integer status);
}