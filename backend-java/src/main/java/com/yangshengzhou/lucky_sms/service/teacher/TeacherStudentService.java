package com.yangshengzhou.lucky_sms.service.teacher;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.vo.teacher.StudentManagementVO;

/**
 * 教师学生管理服务接口
 */
public interface TeacherStudentService {
    
    /**
     * 获取学生列表
     * @param page 页码
     * @param size 每页数量
     * @param keyword 搜索关键词
     * @param teacherId 教师ID
     * @return 学生列表
     */
    StudentManagementVO getStudentList(Integer page, Integer size, String keyword, Integer teacherId);
}
