package com.yangshengzhou.lucky_sms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yangshengzhou.lucky_sms.service.BaseService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 基础服务实现类
 * @param <M> Mapper类型
 * @param <T> 实体类型
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {
    
    @Override
    public Page<T> findPage(int page, int size) {
        Page<T> pageInfo = new Page<>(page, size);
        return this.page(pageInfo);
    }
    
    @Override
    public Page<T> findPageWithQuery(int page, int size, Object query) {
        // 子类实现具体的查询逻辑
        return findPage(page, size);
    }
    
    @Override
    public boolean deleteBatch(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }
    
    @Override
    public boolean updateStatusBatch(List<Integer> ids, Integer status) {
        if (ids == null || ids.isEmpty() || status == null) {
            return false;
        }
        // 子类实现具体的状态更新逻辑
        return true;
    }
}