CREATE DATABASE IF NOT EXISTS Lucky_SMS;
USE Lucky_SMS;

/*
基础字典表（低依赖）
*/

-- 用户表：存储系统所有用户的基础信息
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID（主键）',
    username VARCHAR(50) UNIQUE COMMENT '用户名（全局唯一）',
    password_hash VARCHAR(255) COMMENT '加密后的密码',
    email VARCHAR(100) UNIQUE COMMENT '电子邮箱',
    phone VARCHAR(20) UNIQUE NOT NULL COMMENT '手机号码（不得为空）',
    gender ENUM('M', 'F', 'O') DEFAULT 'O' COMMENT '性别（M-男，F-女，O-其他）',
    birth_date DATE COMMENT '出生日期',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    external_id VARCHAR(50) UNIQUE COMMENT '外部系统ID',
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE' COMMENT '用户状态',
    last_login_time TIMESTAMP COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    last_password_change_time TIMESTAMP COMMENT '密码最后修改时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '用户表-存储用户的基础信息';

-- 角色表：定义系统中的不同角色
CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID（主键）',
    role_name VARCHAR(50) UNIQUE NOT NULL COMMENT '角色名称（全局唯一）',
    description VARCHAR(255) COMMENT '角色描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '角色表-定义系统中的不同角色及权限范围';

-- 用户 - 角色关联表（多对多）
CREATE TABLE user_roles (
    user_id INT NOT NULL COMMENT '用户ID（外键）',
    role_id INT NOT NULL COMMENT '角色ID（外键）',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON UPDATE CASCADE
) COMMENT = '用户角色关联表-定义用户拥有的角色';

-- 权限表：定义系统中的各种操作权限
CREATE TABLE permissions (
    permission_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID（主键）',
    permission_name VARCHAR(50) UNIQUE NOT NULL COMMENT '权限名称（全局唯一）',
    description VARCHAR(255) COMMENT '权限描述',
    module VARCHAR(50) COMMENT '所属功能模块',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '权限表-定义系统操作权限';

-- 角色-权限关联表（多对多）
CREATE TABLE role_permissions (
    role_id INT NOT NULL COMMENT '角色ID（外键）',
    permission_id INT NOT NULL COMMENT '权限ID（外键）',
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT = '角色权限关联表-定义角色与权限的映射关系';

-- 学生状态表
CREATE TABLE student_statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '状态ID（主键）',
    status_name VARCHAR(50) UNIQUE NOT NULL COMMENT '状态名称（全局唯一）',
    description VARCHAR(255) COMMENT '状态描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '学生状态表-定义学生状态（在读、毕业、休学等）';

-- 教师状态表
CREATE TABLE teacher_statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '状态ID（主键）',
    status_name VARCHAR(50) UNIQUE NOT NULL COMMENT '状态名称（全局唯一）',
    description VARCHAR(255) COMMENT '状态描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '教师状态表-定义教师状态（在职、休假、退休等）';

-- 职称表
CREATE TABLE teacher_titles (
    title_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '职称ID（主键）',
    title_name VARCHAR(50) UNIQUE NOT NULL COMMENT '职称名称（全局唯一）',
    title_level TINYINT NOT NULL COMMENT '职称级别（1-初级，2-中级，3-高级）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '教师职称表-定义教师职称及级别';

-- 成绩审核状态表
CREATE TABLE grade_review_statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '状态ID（主键）',
    status_name VARCHAR(50) UNIQUE NOT NULL COMMENT '状态名称（全局唯一）',
    description VARCHAR(255) COMMENT '状态描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '成绩审核状态表-定义成绩审核状态（待审核、已通过、已拒绝）';

-- 图书借阅状态表
CREATE TABLE book_borrow_statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '状态ID（主键）',
    status_name VARCHAR(50) UNIQUE NOT NULL COMMENT '状态名称（全局唯一）',
    description VARCHAR(255) COMMENT '状态描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '图书借阅状态表-定义图书借阅状态（已借出、已归还、已逾期等）';

-- 图书分类表（级联操作）
CREATE TABLE book_categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID（主键）',
    category_name VARCHAR(50) UNIQUE NOT NULL COMMENT '分类名称（全局唯一）',
    parent_id INT COMMENT '父分类ID（外键），NULL表示顶级分类',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (parent_id) REFERENCES book_categories(category_id) ON DELETE SET NULL
) COMMENT = '图书分类表-定义图书分类层级结构';

-- 学院表
CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学院ID（主键）',
    department_name VARCHAR(50) UNIQUE NOT NULL COMMENT '学院名称（全局唯一）',
    department_code VARCHAR(10) UNIQUE NOT NULL COMMENT '学院代码（全局唯一）',
    dean_id INT COMMENT '院长ID（外键，关联用户表）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (dean_id) REFERENCES users(user_id) ON UPDATE SET NULL ON DELETE SET NULL
) COMMENT = '学院表-定义学校学院及负责人';

-- 专业表（级联操作）
CREATE TABLE majors (
    major_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '专业ID（主键）',
    major_name VARCHAR(50) NOT NULL COMMENT '专业名称',
    department_id INT NOT NULL COMMENT '所属学院ID（外键）',
    major_code VARCHAR(10) UNIQUE NOT NULL COMMENT '专业代码（全局唯一）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT = '专业表-定义学院下的专业';

-- 教师表（级联操作）
CREATE TABLE teachers (
    teacher_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '教师ID（主键）',
    user_id INT UNIQUE NOT NULL COMMENT '用户ID（外键）',
    department_id INT NOT NULL COMMENT '学院ID（外键）',
    title_id INT NOT NULL COMMENT '职称ID（外键）',
    hire_date DATE NOT NULL COMMENT '入职日期',
    office_location VARCHAR(50) COMMENT '办公室位置',
    teacher_no VARCHAR(20) UNIQUE NOT NULL COMMENT '教师编号（全局唯一）',
    status_id INT NOT NULL DEFAULT 1 COMMENT '教师状态（外键）',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON UPDATE CASCADE,
    FOREIGN KEY (title_id) REFERENCES teacher_titles(title_id) ON UPDATE CASCADE,
    FOREIGN KEY (status_id) REFERENCES teacher_statuses(status_id) ON UPDATE CASCADE,
    INDEX idx_teacher_no (teacher_no)
) COMMENT = '教师表-存储教师详细信息';

-- 学生表（级联操作）
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学生ID（主键）',
    user_id INT UNIQUE NOT NULL COMMENT '用户ID（外键）',
    department_id INT NOT NULL COMMENT '学院ID（外键）',
    major_id INT NOT NULL COMMENT '专业ID（外键）',
    class_name VARCHAR(50) NOT NULL COMMENT '班级名称',
    enrollment_year YEAR NOT NULL COMMENT '入学年份',
    education_years TINYINT NOT NULL DEFAULT 4 COMMENT '学制（年）',
    student_no VARCHAR(20) UNIQUE NOT NULL COMMENT '学号（全局唯一）',
    status_id INT NOT NULL DEFAULT 1 COMMENT '学生状态（外键）',
    gpa DECIMAL(3,2) DEFAULT 0.00 COMMENT '平均绩点',
    class_rank INT DEFAULT 0 COMMENT '班级排名',
    total_credits DECIMAL(5,2) DEFAULT 0.00 COMMENT '已修学分',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人姓名',
    emergency_phone VARCHAR(20) COMMENT '紧急联系人电话',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON UPDATE CASCADE,
    FOREIGN KEY (major_id) REFERENCES majors(major_id) ON UPDATE CASCADE,
    FOREIGN KEY (status_id) REFERENCES student_statuses(status_id) ON UPDATE CASCADE,
    INDEX idx_student_no (student_no),
    INDEX idx_enrollment_year (enrollment_year)
) COMMENT = '学生表-存储学生详细信息';

-- 课程表（补充级联操作）
CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID（主键）',
    course_code VARCHAR(20) UNIQUE NOT NULL COMMENT '课程代码（全局唯一）',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_description TEXT COMMENT '课程描述',
    department_id INT NOT NULL COMMENT '所属学院ID（外键）',
    credit DECIMAL(2,1) NOT NULL COMMENT '学分',
    course_hours TINYINT NOT NULL COMMENT '课时',
    course_type ENUM('COMPULSORY', 'ELECTIVE') NOT NULL COMMENT '课程类型（必修/选修）',
    exam_type ENUM('CLOSED_BOOK', 'OPEN_BOOK', 'REPORT', 'PRACTICAL') COMMENT '考试类型',
    created_by INT NOT NULL COMMENT '创建人ID（外键）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES teachers(teacher_id) ON UPDATE CASCADE,
    INDEX idx_course_name (course_name)
) COMMENT = '课程表-存储课程基本信息';

-- 学期表
CREATE TABLE semesters (
    semester_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学期ID（主键）',
    academic_year VARCHAR(9) NOT NULL COMMENT '学年（如2023-2024）',
    semester_name VARCHAR(10) NOT NULL COMMENT '学期名称（如第一学期、第二学期）',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    is_current TINYINT(1) DEFAULT 0 COMMENT '是否当前学期（0-否，1-是）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_academic_year (academic_year),
    INDEX idx_is_current (is_current)
) COMMENT = '学期表-定义学期信息';

-- 教师授课表（补充级联操作）
CREATE TABLE teaching_assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '授课ID（主键）',
    teacher_id INT NOT NULL COMMENT '教师ID（外键）',
    course_id INT NOT NULL COMMENT '课程ID（外键）',
    semester_id INT NOT NULL COMMENT '学期ID（外键）',
    classroom VARCHAR(50) COMMENT '教室',
    schedule VARCHAR(100) COMMENT '上课时间安排',
    max_students INT DEFAULT 0 COMMENT '最大学生数',
    current_students INT DEFAULT 0 COMMENT '当前学生数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (semester_id) REFERENCES semesters(semester_id) ON DELETE CASCADE,
    INDEX idx_teacher_course (teacher_id, course_id),
    INDEX idx_semester_course (semester_id, course_id)
) COMMENT = '教师授课表-记录教师授课安排';

-- 学生选课表（补充级联操作）
CREATE TABLE course_selections (
    selection_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '选课ID（主键）',
    student_id INT NOT NULL COMMENT '学生ID（外键）',
    assignment_id INT NOT NULL COMMENT '授课ID（外键）',
    selection_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    status ENUM('SELECTED', 'DROPPED', 'COMPLETED') DEFAULT 'SELECTED' COMMENT '选课状态（SELECTED-已选，DROPPED-已退，COMPLETED-已完成）',
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (assignment_id) REFERENCES teaching_assignments(assignment_id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_assignment (student_id, assignment_id)
) COMMENT = '学生选课表-记录学生选课信息';

-- 成绩表（补充级联操作）
CREATE TABLE course_grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩ID（主键）',
    student_id INT NOT NULL COMMENT '学生ID（外键）',
    assignment_id INT NOT NULL COMMENT '授课ID（外键）',
    usual_score DECIMAL(5,2) COMMENT '平时成绩',
    final_grade DECIMAL(5,2) COMMENT '期末成绩',
    gpa_grade DECIMAL(3,2) COMMENT '绩点',
    review_status_id INT NOT NULL DEFAULT 1 COMMENT '审核状态（外键）',
    review_time TIMESTAMP NULL COMMENT '审核时间',
    reviewer_id INT COMMENT '审核人ID（外键）',
    review_comment TEXT COMMENT '审核意见',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (assignment_id) REFERENCES teaching_assignments(assignment_id) ON DELETE CASCADE,
    FOREIGN KEY (review_status_id) REFERENCES grade_review_statuses(status_id) ON UPDATE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES teachers(teacher_id) ON UPDATE SET NULL,
    UNIQUE KEY unique_student_assignment_grade (student_id, assignment_id),
    INDEX idx_student_grade (student_id),
    INDEX idx_assignment_grade (assignment_id)
) COMMENT = '成绩表-记录学生课程成绩';

-- 图书表
CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '图书ID（主键）',
    isbn VARCHAR(20) UNIQUE NOT NULL COMMENT '国际标准书号（ISBN）',
    book_title VARCHAR(255) NOT NULL COMMENT '书名',
    author VARCHAR(100) NOT NULL COMMENT '作者',
    publisher VARCHAR(100) NOT NULL COMMENT '出版社',
    publish_year INT COMMENT '出版年份',
    category_id INT NOT NULL COMMENT '图书分类ID（外键）',
    location VARCHAR(50) NOT NULL COMMENT '馆藏位置',
    total_copies INT NOT NULL DEFAULT 1 COMMENT '总册数',
    available_copies INT NOT NULL DEFAULT 1 COMMENT '可借册数',
    created_by INT NOT NULL COMMENT '创建人ID（外键）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (category_id) REFERENCES book_categories(category_id) ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON UPDATE CASCADE,
    INDEX idx_book_title (book_title)
) COMMENT = '图书表-记录图书馆藏书信息';

-- 图书预约表
CREATE TABLE book_reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '预约记录ID（主键）',
    user_id INT NOT NULL COMMENT '预约用户ID（外键）',
    book_id INT NOT NULL COMMENT '图书ID（外键）',
    reservation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
    status ENUM('PENDING', 'FULFILLED', 'CANCELLED', 'EXPIRED') DEFAULT 'PENDING' COMMENT '预约状态',
    fulfilled_time TIMESTAMP COMMENT '预约实现时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    UNIQUE KEY unique_pending_reservation (user_id, book_id, status)
) COMMENT = '图书预约表-记录图书预约信息';

-- 图书借阅表（级联操作）
CREATE TABLE book_borrowings (
    borrowing_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '借阅记录ID（主键）',
    user_id INT NOT NULL COMMENT '借阅用户ID（外键）',
    book_id INT NOT NULL COMMENT '图书ID（外键）',
    borrow_date DATE NOT NULL COMMENT '借阅日期',
    due_date DATE NOT NULL COMMENT '应归还日期',
    return_date DATE COMMENT '实际归还日期',
    fine DECIMAL(5,2) DEFAULT 0 COMMENT '罚款金额',
    status_id INT NOT NULL DEFAULT 1 COMMENT '借阅状态（外键）',
    renew_count TINYINT DEFAULT 0 COMMENT '续借次数',
    damage_status ENUM('GOOD', 'DAMAGED', 'LOST') DEFAULT 'GOOD' COMMENT '损坏状态',
    compensation DECIMAL(5,2) DEFAULT 0 COMMENT '赔偿金额',
    created_by INT NOT NULL COMMENT '操作人ID（外键）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON UPDATE CASCADE,
    FOREIGN KEY (status_id) REFERENCES book_borrow_statuses(status_id) ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON UPDATE CASCADE,
    INDEX idx_due_date (due_date)
) COMMENT = '图书借阅表-记录图书借阅信息';

-- 数据变更日志表（补充级联操作）
CREATE TABLE id_changes (
    change_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '变更记录ID（主键）',
    table_name VARCHAR(50) NOT NULL COMMENT '变更的表名',
    record_id INT NOT NULL COMMENT '记录ID',
    old_id VARCHAR(50) NOT NULL COMMENT '旧ID',
    new_id VARCHAR(50) NOT NULL COMMENT '新ID',
    changed_by INT NOT NULL COMMENT '操作人ID（外键）',
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '变更时间',
    reason VARCHAR(255) COMMENT '变更原因',
    FOREIGN KEY (changed_by) REFERENCES users(user_id) ON UPDATE CASCADE
) COMMENT = 'ID变更表-记录重要ID变更历史';

-- 系统配置表
CREATE TABLE system_config (
    config_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID（主键）',
    config_key VARCHAR(50) UNIQUE NOT NULL COMMENT '配置键（全局唯一）',
    config_value VARCHAR(255) NOT NULL COMMENT '配置值',
    config_type ENUM('STRING', 'NUMBER', 'BOOLEAN', 'JSON') NOT NULL DEFAULT 'STRING' COMMENT '配置类型',
    description VARCHAR(255) COMMENT '配置描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '系统配置表-存储系统配置参数';

-- 课程先修关系表（补充级联操作）
CREATE TABLE course_prerequisites (
    course_id INT NOT NULL COMMENT '课程ID（外键）',
    prerequisite_course_id INT NOT NULL COMMENT '先修课程ID（外键）',
    PRIMARY KEY (course_id, prerequisite_course_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (prerequisite_course_id) REFERENCES courses(course_id) ON DELETE CASCADE
) COMMENT = '课程先修关系表-定义课程先修关系';


/*
业务逻辑（存储过程与触发器）
*/

-- 存储过程：更新班级排名
DELIMITER $$
CREATE PROCEDURE update_class_ranking(IN p_student_id INT)
BEGIN
    DECLARE class_name_val VARCHAR(50);
    
    -- 获取学生所在班级
    SELECT class_name INTO class_name_val FROM students WHERE student_id = p_student_id;
    
    -- 直接通过窗口函数计算排名并更新
    UPDATE students s
    JOIN (
        SELECT 
            student_id,
            ROW_NUMBER() OVER (PARTITION BY s.class_name ORDER BY s.gpa DESC, s.total_credits DESC) AS new_rank
        FROM students
        WHERE class_name = class_name_val AND status_id = 1  -- 仅在读学生
    ) AS ranked ON s.student_id = ranked.student_id
    SET s.class_rank = ranked.new_rank
    WHERE s.class_name = class_name_val;
END$$
DELIMITER ;

-- 绩点计算函数
DELIMITER $$
CREATE FUNCTION calculate_gpa(student_id INT) RETURNS DECIMAL(3,2)
DETERMINISTIC
BEGIN
    DECLARE total_weighted_gpa DECIMAL(10,4);
    DECLARE total_credits DECIMAL(5,2);
    
    SELECT 
        SUM(c.credit * cg.gpa_grade),
        SUM(c.credit)
    INTO 
        total_weighted_gpa,
        total_credits
    FROM 
        course_grades cg
    JOIN 
        teaching_assignments ta ON cg.assignment_id = ta.assignment_id
    JOIN 
        courses c ON ta.course_id = c.course_id
    JOIN 
        course_selections cs ON cg.assignment_id = cs.assignment_id AND cg.student_id = cs.student_id
    WHERE 
        cg.student_id = student_id
        AND cg.final_grade IS NOT NULL
        AND cs.status = 'COMPLETED'
        AND cg.review_status_id = 2;  -- 已通过审核
    
    IF total_credits = 0 THEN
        RETURN 0.00;
    ELSE
        RETURN ROUND(total_weighted_gpa / total_credits, 2);
    END IF;
END$$
DELIMITER ;

-- 学期唯一性触发器：确保同一时间只有一个当前学期
DELIMITER $$
CREATE TRIGGER ensure_single_current_semester
BEFORE UPDATE ON semesters
FOR EACH ROW
BEGIN
    IF NEW.is_current = 1 THEN
        UPDATE semesters SET is_current = 0 WHERE is_current = 1 AND semester_id != NEW.semester_id;
    END IF;
END$$
DELIMITER ;

-- 成绩录入触发器：更新学生绩点和排名
DELIMITER $$
CREATE TRIGGER update_student_gpa_after_grade_insert
AFTER INSERT ON course_grades
FOR EACH ROW
BEGIN
    IF NEW.review_status_id = 2 THEN  -- 仅审核通过的成绩更新绩点
        UPDATE students 
        SET 
            gpa = calculate_gpa(NEW.student_id),
            total_credits = COALESCE(  -- 处理可能的NULL值
                (SELECT SUM(c.credit) 
                FROM course_grades cg
                JOIN teaching_assignments ta ON cg.assignment_id = ta.assignment_id
                JOIN courses c ON ta.course_id = c.course_id
                JOIN course_selections cs ON cg.assignment_id = cs.assignment_id AND cg.student_id = cs.student_id
                WHERE cg.student_id = NEW.student_id
                AND cg.final_grade >= 60
                AND cs.status = 'COMPLETED'
                AND cg.review_status_id = 2),
                0.00
            )
        WHERE student_id = NEW.student_id;
        
        CALL update_class_ranking(NEW.student_id);
    END IF;
END$$
DELIMITER ;

-- 成绩更新触发器：更新学生绩点和排名
DELIMITER $$
CREATE TRIGGER update_student_gpa_after_grade_update
AFTER UPDATE ON course_grades
FOR EACH ROW
BEGIN
    IF (OLD.review_status_id != 2 AND NEW.review_status_id = 2) 
       OR (OLD.final_grade != NEW.final_grade AND NEW.review_status_id = 2) THEN
        UPDATE students 
        SET 
            gpa = calculate_gpa(NEW.student_id),
            total_credits = COALESCE(  -- 处理可能的NULL值
                (SELECT SUM(c.credit) 
                FROM course_grades cg
                JOIN teaching_assignments ta ON cg.assignment_id = ta.assignment_id
                JOIN courses c ON ta.course_id = c.course_id
                JOIN course_selections cs ON cg.assignment_id = cs.assignment_id AND cg.student_id = cs.student_id
                WHERE cg.student_id = NEW.student_id
                AND cg.final_grade >= 60
                AND cs.status = 'COMPLETED'
                AND cg.review_status_id = 2),
                0.00
            )
        WHERE student_id = NEW.student_id;
        
        CALL update_class_ranking(NEW.student_id);
    END IF;
END$$
DELIMITER ;

-- 图书借阅触发器：借阅时减少可用数量
DELIMITER $$
CREATE TRIGGER update_book_available_after_borrow
AFTER INSERT ON book_borrowings
FOR EACH ROW
BEGIN
    -- 仅当借阅状态为"已借出"时更新可用数量
    IF NEW.status_id = 1 THEN
        UPDATE books 
        SET available_copies = available_copies - 1 
        WHERE book_id = NEW.book_id;
    END IF;
END$$
DELIMITER ;

-- 图书归还触发器：归还时增加可用数量
DELIMITER $$
CREATE TRIGGER update_book_available_after_return
AFTER UPDATE ON book_borrowings
FOR EACH ROW
BEGIN
    -- 当状态从"已借出"/"已逾期"变为"已归还"时更新可用数量
    IF (OLD.status_id IN (1, 3) AND NEW.status_id = 2) THEN
        UPDATE books 
        SET available_copies = available_copies + 1 
        WHERE book_id = NEW.book_id;
    END IF;
END$$
DELIMITER ;

-- 选课人数增加触发器
DELIMITER $$
CREATE TRIGGER update_current_students_after_insert
AFTER INSERT ON course_selections
FOR EACH ROW
BEGIN
    DECLARE error_msg VARCHAR(255);
    
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
    BEGIN
        GET DIAGNOSTICS CONDITION 1 error_msg = MESSAGE_TEXT;
        -- 记录错误到系统日志表（如果存在）
        INSERT INTO system_config (config_key, config_value, config_type, description)
        VALUES ('TRIGGER_ERROR', CONCAT('update_current_students_after_insert: ', error_msg), 'STRING', '触发器错误日志')
        ON DUPLICATE KEY UPDATE config_value = CONCAT('update_current_students_after_insert: ', error_msg);
    END;
    
    IF NEW.status = 'SELECTED' THEN
        UPDATE teaching_assignments 
        SET current_students = current_students + 1 
        WHERE assignment_id = NEW.assignment_id;
    END IF;
END$$
DELIMITER ;

-- 选课人数减少触发器
DELIMITER $$
CREATE TRIGGER update_current_students_after_update
AFTER UPDATE ON course_selections
FOR EACH ROW
BEGIN
    DECLARE error_msg VARCHAR(255);
    
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
    BEGIN
        GET DIAGNOSTICS CONDITION 1 error_msg = MESSAGE_TEXT;
        -- 记录错误到系统日志表（如果存在）
        INSERT INTO system_config (config_key, config_value, config_type, description)
        VALUES ('TRIGGER_ERROR', CONCAT('update_current_students_after_update: ', error_msg), 'STRING', '触发器错误日志')
        ON DUPLICATE KEY UPDATE config_value = CONCAT('update_current_students_after_update: ', error_msg);
    END;
    
    IF OLD.status = 'SELECTED' AND NEW.status = 'DROPPED' THEN
        UPDATE teaching_assignments 
        SET current_students = current_students - 1 
        WHERE assignment_id = NEW.assignment_id;
    END IF;
END$$
DELIMITER ;


/*
视图定义
*/

-- 班级排名视图
CREATE OR REPLACE VIEW class_ranking AS
SELECT 
    s.class_name,
    s.student_id,
    s.student_no,
    u.username,
    s.gpa,
    RANK() OVER (PARTITION BY s.class_name ORDER BY s.gpa DESC) AS `class_rank`
FROM 
    students s
JOIN 
    users u ON s.user_id = u.user_id
JOIN 
    student_statuses ss ON s.status_id = ss.status_id
WHERE 
    ss.status_name = 'ACTIVE';

-- 课程平均分视图
CREATE OR REPLACE VIEW course_statistics AS
SELECT 
    c.course_id,
    c.course_name,
    s.semester_id,
    s.academic_year,
    s.semester_name,
    COUNT(cg.grade_id) AS total_students,
    AVG(cg.final_grade) AS avg_grade,
    MIN(cg.final_grade) AS min_grade,
    MAX(cg.final_grade) AS max_grade
FROM 
    courses c
JOIN 
    teaching_assignments ta ON c.course_id = c.course_id
JOIN 
    semesters s ON ta.semester_id = s.semester_id
LEFT JOIN 
    course_grades cg ON ta.assignment_id = cg.assignment_id
WHERE
    cg.review_status_id = 2  -- 仅包含已审核通过的成绩
GROUP BY 
    c.course_id, c.course_name, s.semester_id, s.academic_year, s.semester_name;

-- 学生课程统计视图
CREATE OR REPLACE VIEW student_course_stats AS
SELECT 
    s.student_id,
    s.student_no,
    u.username,
    d.department_name,
    m.major_name,
    s.class_name,
    COUNT(cs.selection_id) AS total_courses,
    SUM(CASE WHEN cs.status = 'COMPLETED' THEN 1 ELSE 0 END) AS completed_courses,
    SUM(CASE WHEN cs.status = 'COMPLETED' THEN c.credit ELSE 0 END) AS total_credits,
    AVG(cg.final_grade) AS avg_grade
FROM 
    students s
JOIN 
    users u ON s.user_id = u.user_id
JOIN 
    departments d ON s.department_id = d.department_id
JOIN 
    majors m ON s.major_id = m.major_id
LEFT JOIN 
    course_selections cs ON s.student_id = cs.student_id
LEFT JOIN 
    teaching_assignments ta ON cs.assignment_id = ta.assignment_id
LEFT JOIN 
    courses c ON ta.course_id = c.course_id
LEFT JOIN 
    course_grades cg ON cg.student_id = cs.student_id AND cg.assignment_id = cs.assignment_id
WHERE
    cg.review_status_id = 2  -- 仅包含已审核通过的成绩
GROUP BY 
    s.student_id, s.student_no, u.username, d.department_name, m.major_name, s.class_name;

-- 教师授课统计视图
CREATE OR REPLACE VIEW teacher_course_stats AS
SELECT 
    t.teacher_id,
    t.teacher_no,
    u.username,
    d.department_name,
    tt.title_name,
    COUNT(ta.assignment_id) AS total_assignments,
    SUM(ta.current_students) AS total_students,
    AVG(ta.current_students) AS avg_students_per_course
FROM 
    teachers t
JOIN 
    users u ON t.user_id = u.user_id
JOIN 
    departments d ON t.department_id = d.department_id
JOIN 
    teacher_titles tt ON t.title_id = tt.title_id
LEFT JOIN 
    teaching_assignments ta ON t.teacher_id = ta.teacher_id
GROUP BY 
    t.teacher_id, t.teacher_no, u.username, d.department_name, tt.title_name;


/*
初始化数据
*/

-- 初始化系统配置
INSERT INTO system_config (config_key, config_value, config_type, description) VALUES
('student_id_format', 'YYYY{dept}{major}####', 'STRING', '学号格式：入学年份+学院代码+专业代码+4位序号'),
('teacher_id_format', 'YY{dept}####', 'STRING', '教师编号格式：入职年份后两位+学院代码+4位序号'),
('max_borrow_days', '30', 'NUMBER', '图书最大借阅天数'),
('fine_per_day', '1.00', 'NUMBER', '图书逾期每日罚款金额'),
('semester_course_limit', '5', 'NUMBER', '学生每学期最多选课数量'),
('gpa_scale', '4.0', 'NUMBER', '绩点满分值'),
('gpa_calculation_method', 'weighted_average', 'STRING', '绩点计算方法：加权平均'),
('max_renew_count', '2', 'NUMBER', '图书最大续借次数'),
('password_expiration_days', '90', 'NUMBER', '密码过期天数');

-- 初始化角色数据
INSERT INTO roles (role_name, description) VALUES
('ADMIN', '系统管理员'),
('TEACHER', '教师'),
('STUDENT', '学生'),
('LIBRARIAN', '图书管理员'),
('ASSISTANT', '助教');

-- 初始化权限数据（移除聊天相关权限）
INSERT INTO permissions (permission_name, description, module) VALUES
-- 用户管理模块
('CREATE_USER', '创建用户', '用户管理'),
('EDIT_USER', '编辑用户', '用户管理'),
('DELETE_USER', '删除用户', '用户管理'),
('VIEW_USER', '查看用户', '用户管理'),
-- 权限管理模块
('CREATE_ROLE', '创建角色', '权限管理'),
('EDIT_ROLE', '编辑角色', '权限管理'),
('DELETE_ROLE', '删除角色', '权限管理'),
('ASSIGN_PERMISSION', '分配权限', '权限管理'),
-- 教学管理模块（课程）
('CREATE_COURSE', '创建课程', '教学管理'),
('EDIT_COURSE', '编辑课程', '教学管理'),
('DELETE_COURSE', '删除课程', '教学管理'),
('VIEW_COURSE', '查看课程', '教学管理'),
-- 教学管理模块（成绩）
('INPUT_GRADE', '录入成绩', '教学管理'),
('EDIT_GRADE', '编辑成绩', '教学管理'),
('REVIEW_GRADE', '审核成绩', '教学管理'),
('VIEW_GRADE', '查看成绩', '教学管理'),
-- 图书馆管理模块（图书）
('CREATE_BOOK', '新增图书', '图书馆管理'),
('EDIT_BOOK', '编辑图书', '图书馆管理'),
('DELETE_BOOK', '删除图书', '图书馆管理'),
('VIEW_BOOK', '查看图书', '图书馆管理'),
-- 图书馆管理模块（借阅）
('BORROW_BOOK', '借阅图书', '图书馆管理'),
('RETURN_BOOK', '归还图书', '图书馆管理'),
('RENEW_BOOK', '续借图书', '图书馆管理'),
-- 系统分析模块
('VIEW_STATISTICS', '查看统计数据', '系统分析');

-- 初始化角色-权限映射（移除聊天相关权限）
INSERT INTO role_permissions (role_id, permission_id) VALUES
-- 管理员（1）：所有权限（1-22）
(1,1), (1,2), (1,3), (1,4), (1,5), (1,6), (1,7), (1,8),
(1,9), (1,10), (1,11), (1,12), (1,13), (1,14), (1,15), (1,16),
(1,17), (1,18), (1,19), (1,20), (1,21), (1,22),
-- 教师（2）：课程+成绩+统计权限
(2,12), (2,13), (2,14), (2,15), (2,16), (2,22),
-- 学生（3）：查看课程+成绩+借阅权限
(3,12), (3,16), (3,20),
-- 图书管理员（4）：图书管理+借阅权限
(4,17), (4,18), (4,19), (4,20), (4,21),
-- 助教（5）：查看课程+录入成绩权限
(5,12), (5,13), (5,16);

-- 初始化学生状态
INSERT INTO student_statuses (status_name, description) VALUES
('ACTIVE', '在读'),
('GRADUATED', '已毕业'),
('SUSPENDED', '休学'),
('TRANSFERRED', '转学'),
('LEAVE_SICK', '病假'),
('LEAVE_PERSONAL', '事假'),
('DROP_OUT', '退学');

-- 初始化教师状态
INSERT INTO teacher_statuses (status_name, description) VALUES
('ACTIVE', '在职'),
('INACTIVE', '待岗'),
('RETIRED', '退休'),
('ON_LEAVE', '休假');

-- 初始化成绩审核状态
INSERT INTO grade_review_statuses (status_name, description) VALUES
('PENDING', '待审核'),
('APPROVED', '已通过'),
('REJECTED', '已拒绝');

-- 初始化图书借阅状态
INSERT INTO book_borrow_statuses (status_name, description) VALUES
('BORROWED', '已借出'),
('RETURNED', '已归还'),
('OVERDUE', '已逾期'),
('LOST', '已丢失');

-- 初始化职称表
INSERT INTO teacher_titles (title_name, title_level) VALUES
('助教', 1),
('讲师', 1),
('副教授', 2),
('教授', 3);

-- 初始化学院表
INSERT INTO departments (department_name, department_code) VALUES
('计算机科学与技术学院', 'CS'),
('信息工程学院', 'IE'),
('经济管理学院', 'EM'),
('人文艺术学院', 'HA');

-- 初始化专业表
INSERT INTO majors (major_name, department_id, major_code) VALUES
('计算机科学与技术', 1, 'CS01'),
('软件工程', 1, 'CS02'),
('网络工程', 1, 'CS03'),
('信息管理与信息系统', 2, 'IE01'),
('电子商务', 2, 'IE02'),
('国际经济与贸易', 3, 'EM01'),
('市场营销', 3, 'EM02'),
('汉语言文学', 4, 'HA01'),
('英语', 4, 'HA02');

-- 初始化图书分类表
INSERT INTO book_categories (category_name, parent_id) VALUES
('计算机科学', NULL),
('文学', NULL),
('经济管理', NULL),
('编程语言', 1),
('数据库', 1),
('网络技术', 1),
('中国文学', 2),
('外国文学', 2),
('经济学', 3),
('管理学', 3);

-- 初始化学期表
INSERT INTO semesters (academic_year, semester_name, start_date, end_date, is_current) VALUES
('2023-2024', '第一学期', '2023-09-01', '2024-01-20', 0),
('2023-2024', '第二学期', '2024-02-26', '2024-07-15', 1),
('2024-2025', '第一学期', '2024-09-01', '2025-01-20', 0);

-- 初始化用户表（管理员）
INSERT INTO users (username, password_hash, email, phone, gender, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi', 'admin@lucky-sms.com', '13800138000', 'M', 'ACTIVE');

-- 为管理员分配角色（管理员角色ID为1）
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

-- 初始化教师表（管理员也作为教师）
INSERT INTO teachers (user_id, department_id, title_id, hire_date, office_location, teacher_no, status_id) VALUES
(1, 1, 4, '2020-09-01', '计算机楼A301', 'T2020001', 1);

-- 初始化用户表（教师）
INSERT INTO users (username, password_hash, email, phone, gender, status) VALUES
('teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi', 'teacher1@lucky-sms.com', '13800138001', 'M', 'ACTIVE'),
('teacher2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi', 'teacher2@lucky-sms.com', '13800138002', 'F', 'ACTIVE');

-- 为教师分配角色（教师角色ID为2）
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2), (3, 2);

-- 初始化教师表
INSERT INTO teachers (user_id, department_id, title_id, hire_date, office_location, teacher_no, status_id) VALUES
(2, 1, 3, '2021-09-01', '计算机楼A302', 'T2021001', 1),
(3, 2, 2, '2022-09-01', '信息楼B201', 'T2022001', 1);

-- 初始化课程表（现在教师记录已存在）
INSERT INTO courses (course_code, course_name, course_description, department_id, credit, course_hours, course_type, exam_type, created_by) VALUES
('CS101', '计算机基础', '计算机基础知识与操作', 1, 3.0, 48, 'COMPULSORY', 'CLOSED_BOOK', 1),
('CS102', 'C语言程序设计', 'C语言编程基础', 1, 4.0, 64, 'COMPULSORY', 'PRACTICAL', 1),
('CS201', '数据结构', '常用数据结构与算法', 1, 4.0, 64, 'COMPULSORY', 'PRACTICAL', 1),
('CS202', '数据库原理', '数据库系统原理与应用', 1, 3.0, 48, 'COMPULSORY', 'CLOSED_BOOK', 1),
('CS301', '操作系统', '计算机操作系统原理', 1, 4.0, 64, 'COMPULSORY', 'CLOSED_BOOK', 1),
('IE101', '信息系统分析', '信息系统分析与设计', 2, 3.0, 48, 'COMPULSORY', 'REPORT', 1),
('EM101', '宏观经济学', '宏观经济学原理', 3, 3.0, 48, 'COMPULSORY', 'CLOSED_BOOK', 1),
('HA101', '现代汉语', '现代汉语语法与词汇', 4, 2.0, 32, 'ELECTIVE', 'OPEN_BOOK', 1);

-- 初始化用户表（学生）
INSERT INTO users (username, password_hash, email, phone, gender, status) VALUES
('student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi', 'student1@lucky-sms.com', '13800138003', 'M', 'ACTIVE'),
('student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi', 'student2@lucky-sms.com', '13800138004', 'F', 'ACTIVE'),
('student3', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVYITi', 'student3@lucky-sms.com', '13800138005', 'M', 'ACTIVE');

-- 为学生分配角色（学生角色ID为3）
INSERT INTO user_roles (user_id, role_id) VALUES (4, 3), (5, 3), (6, 3);

-- 初始化学生表
INSERT INTO students (user_id, department_id, major_id, class_name, enrollment_year, education_years, student_no, status_id, emergency_contact, emergency_phone) VALUES
(4, 1, 1, '计算机2101班', 2021, 4, '2021CS01001', 1, '张父', '13900138001'),
(5, 1, 2, '软件工程2101班', 2021, 4, '2021CS02001', 1, '李母', '13900138002'),
(6, 2, 4, '信管2101班', 2021, 4, '2021IE01001', 1, '王父', '13900138003');

-- 初始化教师授课表
INSERT INTO teaching_assignments (teacher_id, course_id, semester_id, classroom, schedule, max_students, current_students) VALUES
(2, 1, 2, '计算机楼A101', '周一 8:00-9:40', 50, 30),
(2, 2, 2, '计算机楼B201', '周二 10:00-11:40', 40, 35),
(3, 6, 2, '信息楼C101', '周三 14:00-15:40', 45, 25),
(1, 4, 2, '计算机楼A301', '周四 8:00-9:40', 40, 20);

-- 初始化学生选课表（使用子查询获取正确的ID）
INSERT INTO course_selections (student_id, assignment_id, status)
SELECT s.student_id, ta.assignment_id, 'SELECTED'
FROM students s
CROSS JOIN teaching_assignments ta
WHERE s.student_no IN ('2021CS01001', '2021CS02001', '2021IE01001')
AND ta.assignment_id IN (
    SELECT assignment_id 
    FROM teaching_assignments 
    WHERE (teacher_id = 2 AND course_id IN (1, 2)) 
       OR (teacher_id = 3 AND course_id = 6) 
       OR (teacher_id = 1 AND course_id = 4)
)
AND (
    (s.student_no = '2021CS01001' AND ta.course_id IN (1, 2)) OR
    (s.student_no = '2021CS02001' AND ta.course_id IN (2, 4)) OR
    (s.student_no = '2021IE01001' AND ta.course_id IN (6, 4))
);

-- 初始化图书表
INSERT INTO books (isbn, book_title, author, publisher, publish_year, category_id, location, total_copies, available_copies, created_by)
SELECT 
    b.isbn, b.book_title, b.author, b.publisher, b.publish_year, b.category_id, b.location, b.total_copies, b.available_copies,
    (SELECT user_id FROM users WHERE username = 'admin' LIMIT 1) AS created_by
FROM (
    SELECT '9787111213826' AS isbn, 'Java编程思想' AS book_title, 'Bruce Eckel' AS author, '机械工业出版社' AS publisher, 2007 AS publish_year, 4 AS category_id, '计算机馆A区101' AS location, 5 AS total_copies, 3 AS available_copies
    UNION ALL SELECT '9787115279460', 'Python编程：从入门到实践', 'Eric Matthes', '人民邮电出版社', 2016, 4, '计算机馆A区102', 8, 5
    UNION ALL SELECT '9787302523905', '数据结构与算法分析', 'Mark Allen Weiss', '清华大学出版社', 2019, 5, '计算机馆A区103', 6, 4
    UNION ALL SELECT '9787111565839', '数据库系统概念', 'Abraham Silberschatz', '机械工业出版社', 2020, 5, '计算机馆A区104', 4, 2
    UNION ALL SELECT '9787040507215', '红楼梦', '曹雪芹', '高等教育出版社', 2019, 7, '文学馆B区201', 3, 2
    UNION ALL SELECT '9787040518952', '经济学原理', 'N. Gregory Mankiw', '高等教育出版社', 2020, 9, '经管馆C区301', 5, 3
) AS b;

-- 初始化图书借阅表
INSERT INTO book_borrowings (user_id, book_id, borrow_date, due_date, status_id, created_by) VALUES
(4, 1, '2023-10-01', '2023-10-31', 2, 1),
(5, 2, '2023-10-05', '2023-11-04', 1, 1),
(6, 3, '2023-10-10', '2023-11-09', 1, 1);