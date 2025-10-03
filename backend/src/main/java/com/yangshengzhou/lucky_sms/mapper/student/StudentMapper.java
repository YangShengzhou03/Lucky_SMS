package com.yangshengzhou.lucky_sms.mapper.student;

import com.yangshengzhou.lucky_sms.vo.student.StudentVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper {
    // 根据userId返回student数据
    StudentVO selectStudentById(Integer userId);

}
