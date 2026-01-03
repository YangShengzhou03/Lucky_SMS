package com.yangshengzhou.lucky_sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 基础服务接口
 * @param <T> 实体类型
 */
public interface BaseService<T> extends IService<T> {
    
    /**
     * 分页查询
     * @param page 页码
     * @param size 每页数量
     * @return 分页结果
     */
    Page<T> findPage(int page, int size);
    
    /**
     * 条件分页查询
     * @param page 页码
     * @param size 每页数量
     * @param query 查询条件
     * @return 分页结果
     */
    Page<T> findPageWithQuery(int page, int size, Object query);
    
    /**
     * 批量删除
     * @param ids ID列表
     * @return 是否成功
     */
    boolean deleteBatch(List<Integer> ids);
    
    /**
     * 批量更新状态
     * @param ids ID列表
     * @param status 状态值
     * @return 是否成功
     */
    boolean updateStatusBatch(List<Integer> ids, Integer status);
}