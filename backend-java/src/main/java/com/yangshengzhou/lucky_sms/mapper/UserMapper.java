package com.yangshengzhou.lucky_sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yangshengzhou.lucky_sms.pojo.User;
import com.yangshengzhou.lucky_sms.vo.user.LoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    LoginVO loginByPhone(String phone);

    int registerByPhone(String phone);

    int resetPasswordByPhone(String phone, String password);

    LoginVO loginByPassword(String phone, String password);

    @Select("SELECT u.*, ut.type_name as role_name, d.department_name " +
            "FROM users u " +
            "LEFT JOIN user_types ut ON u.user_type_id = ut.user_type_id " +
            "LEFT JOIN departments d ON u.user_id = d.dean_user_id " +
            "WHERE u.deleted = 0 " +
            "AND (u.username LIKE CONCAT('%', #{keyword}, '%') " +
            "OR u.real_name LIKE CONCAT('%', #{keyword}, '%') " +
            "OR u.phone LIKE CONCAT('%', #{keyword}, '%')) " +
            "AND (#{role} IS NULL OR ut.type_code = #{role})")
    IPage<Map<String, Object>> getUserList(Page<Map<String, Object>> page, 
                                          @Param("keyword") String keyword, 
                                          @Param("role") String role);

    @Select("SELECT COUNT(*) FROM users WHERE deleted = 0")
    Long getTotalUsers();

    @Select("SELECT COUNT(*) FROM users u INNER JOIN user_types ut ON u.user_type_id = ut.user_type_id WHERE ut.type_code = 'STUDENT' AND u.deleted = 0")
    Long getTotalStudents();

    @Select("SELECT COUNT(*) FROM users u INNER JOIN user_types ut ON u.user_type_id = ut.user_type_id WHERE ut.type_code = 'TEACHER' AND u.deleted = 0")
    Long getTotalTeachers();

    @Select("SELECT COUNT(*) FROM courses WHERE deleted = 0")
    Long getTotalCourses();

    @Select("SELECT COUNT(*) FROM departments WHERE deleted = 0")
    Long getTotalDepartments();
}
