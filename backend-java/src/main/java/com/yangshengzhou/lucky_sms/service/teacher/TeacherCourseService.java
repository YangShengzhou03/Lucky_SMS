package com.yangshengzhou.lucky_sms.service.teacher;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.vo.teacher.CourseManagementVO;

/**
 * 教师课程管理服务接口
 */
public interface TeacherCourseService {
    
    /**
     * 获取课程列表
     * @param page 页码
     * @param size 每页数量
     * @param semester 学期
     * @param teacherId 教师ID
     * @return 课程列表
     */
    CourseManagementVO getCourseList(Integer page, Integer size, String semester, Integer teacherId);
}
