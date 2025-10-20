-- 1. 强制 SQL 执行错误时中断，暴露问题
SET SQL_MODE = 'STRICT_ALL_TABLES';
-- 2. 关闭外键检查，避免建表顺序导致的外键失败
SET FOREIGN_KEY_CHECKS = 0;

-- 删除现有数据库并重新创建
DROP DATABASE IF EXISTS Lucky_SMS;
CREATE DATABASE Lucky_SMS CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE Lucky_SMS;

/*
第一阶段：基础字典表（无外键依赖）
*/

-- 用户表：存储系统所有用户的基础信息（先创建基础表，自引用外键稍后添加）
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID（主键）',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password_hash VARCHAR(255) COMMENT '加密后的密码',
    email VARCHAR(100) UNIQUE COMMENT '电子邮箱',
    phone VARCHAR(20) UNIQUE NOT NULL COMMENT '手机号码',
    gender ENUM('M', 'F', 'O') DEFAULT 'O' COMMENT '性别（M-男，F-女，O-其他）',
    birth_date DATE COMMENT '出生日期',
    avatar_url VARCHAR(255) COMMENT '头像URL',
    external_id VARCHAR(50) UNIQUE COMMENT '外部系统ID',
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE' COMMENT '用户状态',
    last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    last_password_change_time TIMESTAMP NULL COMMENT '密码最后修改时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by INT COMMENT '创建人ID',
    updated_by INT COMMENT '更新人ID',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_status (status),
    CONSTRAINT chk_birth_date CHECK (birth_date IS NULL OR birth_date >= '1900-01-01')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '用户表-存储用户的基础信息';

-- 角色表：定义系统中的不同角色
CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID（主键）',
    role_name VARCHAR(50) UNIQUE NOT NULL COMMENT '角色名称（全局唯一）',
    description VARCHAR(255) COMMENT '角色描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_role_name (role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '角色表-定义系统中的不同角色及权限范围';

-- 权限表：定义系统中的各种操作权限
CREATE TABLE permissions (
    permission_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID（主键）',
    permission_name VARCHAR(50) UNIQUE NOT NULL COMMENT '权限名称（全局唯一）',
    description VARCHAR(255) COMMENT '权限描述',
    module VARCHAR(50) NOT NULL COMMENT '所属功能模块',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_permission_name (permission_name),
    INDEX idx_module (module)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '权限表-定义系统操作权限';

-- 学生状态表
CREATE TABLE student_statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '状态ID（主键）',
    status_name VARCHAR(50) UNIQUE NOT NULL COMMENT '状态名称（全局唯一）',
    description VARCHAR(255) COMMENT '状态描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status_name (status_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '学生状态表-定义学生状态（在读、毕业、休学等）';

-- 教师状态表
CREATE TABLE teacher_statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '状态ID（主键）',
    status_name VARCHAR(50) UNIQUE NOT NULL COMMENT '状态名称（全局唯一）',
    description VARCHAR(255) COMMENT '状态描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status_name (status_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '教师状态表-定义教师状态（在职、休假、退休等）';

-- 职称表
CREATE TABLE teacher_titles (
    title_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '职称ID（主键）',
    title_name VARCHAR(50) UNIQUE NOT NULL COMMENT '职称名称（全局唯一）',
    title_level TINYINT NOT NULL COMMENT '职称级别（1-初级，2-中级，3-高级）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_title_name (title_name),
    INDEX idx_title_level (title_level),
    CONSTRAINT chk_title_level CHECK (title_level IN (1, 2, 3))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '教师职称表-定义教师职称及级别';

-- 成绩审核状态表
CREATE TABLE grade_review_statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '状态ID（主键）',
    status_name VARCHAR(50) UNIQUE NOT NULL COMMENT '状态名称（全局唯一）',
    description VARCHAR(255) COMMENT '状态描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status_name (status_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '成绩审核状态表-定义成绩审核状态（待审核、已通过、已拒绝）';

-- 图书借阅状态表
CREATE TABLE book_borrow_statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '状态ID（主键）',
    status_name VARCHAR(50) UNIQUE NOT NULL COMMENT '状态名称（全局唯一）',
    description VARCHAR(255) COMMENT '状态描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status_name (status_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '图书借阅状态表-定义图书借阅状态（已借出、已归还、已逾期等）';

-- 图书分类表（级联操作）
CREATE TABLE book_categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID（主键）',
    category_name VARCHAR(50) UNIQUE NOT NULL COMMENT '分类名称（全局唯一）',
    parent_id INT COMMENT '父分类ID（外键），NULL表示顶级分类',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (parent_id) REFERENCES book_categories(category_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '图书分类表-定义图书分类层级结构';

/*
第二阶段：基础业务表（依赖基础字典表）
*/

-- 学院表（先创建基础表，院长外键稍后添加）
CREATE TABLE departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学院ID（主键）',
    department_name VARCHAR(50) UNIQUE NOT NULL COMMENT '学院名称（全局唯一）',
    department_code VARCHAR(10) UNIQUE NOT NULL COMMENT '学院代码（全局唯一）',
    dean_id INT COMMENT '院长ID（外键，关联教师表）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_department_code (department_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '学院表-定义学校学院及负责人';

-- 专业表（级联操作）
CREATE TABLE majors (
    major_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '专业ID（主键）',
    major_name VARCHAR(50) NOT NULL COMMENT '专业名称',
    department_id INT NOT NULL COMMENT '所属学院ID（外键）',
    major_code VARCHAR(10) UNIQUE NOT NULL COMMENT '专业代码（全局唯一）',
    required_credits DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '应修学分',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_major_code (major_code),
    INDEX idx_department_id (department_id),
    CONSTRAINT chk_required_credits CHECK (required_credits >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '专业表-定义学院下的专业';

-- 班级表（级联操作，班主任外键稍后添加）
CREATE TABLE class_info (
    class_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID（主键）',
    class_name VARCHAR(50) UNIQUE NOT NULL COMMENT '班级名称（全局唯一）',
    major_id INT NOT NULL COMMENT '所属专业ID（外键）',
    enrollment_year YEAR NOT NULL COMMENT '入学年份',
    class_advisor_id INT COMMENT '班主任ID（外键，关联教师表）',
    classroom VARCHAR(50) COMMENT '固定教室',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (major_id) REFERENCES majors(major_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_class_name (class_name),
    INDEX idx_major_id (major_id),
    INDEX idx_enrollment_year (enrollment_year),
    CONSTRAINT chk_enrollment_year CHECK (enrollment_year >= 2000)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '班级表-定义专业下的班级信息';

-- 用户角色关联表
CREATE TABLE user_roles (
    user_id INT NOT NULL COMMENT '用户ID（外键）',
    role_id INT NOT NULL COMMENT '角色ID（外键）',
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
    assigned_by INT COMMENT '分配人ID（外键）',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (assigned_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '用户角色关联表-记录用户与角色的对应关系';

-- 角色权限关联表
CREATE TABLE role_permissions (
    role_id INT NOT NULL COMMENT '角色ID（外键）',
    permission_id INT NOT NULL COMMENT '权限ID（外键）',
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '分配时间',
    assigned_by INT COMMENT '分配人ID（外键）',
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (assigned_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '角色权限关联表-记录角色与权限的对应关系';

-- 教师表（修正外键引用）
CREATE TABLE teachers (
    teacher_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '教师ID（主键）',
    user_id INT UNIQUE NOT NULL COMMENT '用户ID（外键）',
    department_id INT NOT NULL COMMENT '学院ID（外键）',
    title_id INT NOT NULL COMMENT '职称ID（外键）',
    hire_date DATE NOT NULL COMMENT '入职日期',
    office_location VARCHAR(50) COMMENT '办公室位置',
    teacher_no VARCHAR(20) UNIQUE NOT NULL COMMENT '教师编号（全局唯一）',
    status_id INT NOT NULL DEFAULT 1 COMMENT '教师状态（外键）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by INT COMMENT '创建人ID',
    updated_by INT COMMENT '更新人ID',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (title_id) REFERENCES teacher_titles(title_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (status_id) REFERENCES teacher_statuses(status_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_teacher_no (teacher_no),
    INDEX idx_department_id (department_id),
    INDEX idx_status_id (status_id),
    INDEX idx_teachers_department_status (department_id, status_id),
    CONSTRAINT chk_hire_date CHECK (hire_date >= '2000-01-01')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '教师表-存储教师详细信息';

-- 修正院长外键引用（在teachers表创建后）
ALTER TABLE departments
    ADD CONSTRAINT fk_departments_dean
        FOREIGN KEY (dean_id) REFERENCES teachers(teacher_id) ON UPDATE SET NULL ON DELETE SET NULL;

-- 修正班级表的班主任外键（在teachers表创建后）
ALTER TABLE class_info
    ADD CONSTRAINT fk_class_advisor
        FOREIGN KEY (class_advisor_id) REFERENCES teachers(teacher_id) ON UPDATE SET NULL ON DELETE SET NULL;

-- 学生表（移除冗余字段，修正外键）
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学生ID（主键）',
    user_id INT UNIQUE NOT NULL COMMENT '用户ID（外键）',
    department_id INT NOT NULL COMMENT '学院ID（外键）',
    major_id INT NOT NULL COMMENT '专业ID（外键）',
    class_id INT NOT NULL COMMENT '班级ID（外键）',
    enrollment_date DATE NOT NULL COMMENT '入学日期',
    education_years TINYINT UNSIGNED NOT NULL DEFAULT 4 COMMENT '学制（年）',
    student_no VARCHAR(20) UNIQUE NOT NULL COMMENT '学号（全局唯一）',
    status_id INT NOT NULL DEFAULT 1 COMMENT '学生状态（外键）',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人姓名',
    emergency_phone VARCHAR(20) COMMENT '紧急联系人电话',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by INT COMMENT '创建人ID',
    updated_by INT COMMENT '更新人ID',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (major_id) REFERENCES majors(major_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (class_id) REFERENCES class_info(class_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (status_id) REFERENCES student_statuses(status_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_student_no (student_no),
    INDEX idx_enrollment_date (enrollment_date),
    INDEX idx_class_id (class_id),
    INDEX idx_status_id (status_id),
    INDEX idx_students_class_status (class_id, status_id),
    CONSTRAINT chk_education_years CHECK (education_years BETWEEN 1 AND 8),
    CONSTRAINT chk_enrollment_date CHECK (enrollment_date >= '2000-01-01')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '学生表-存储学生详细信息';

-- 课程表
CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID（主键）',
    course_code VARCHAR(20) UNIQUE NOT NULL COMMENT '课程代码（全局唯一）',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_description TEXT COMMENT '课程描述',
    department_id INT NOT NULL COMMENT '所属学院ID（外键）',
    credit DECIMAL(4,1) NOT NULL COMMENT '学分（必须大于0）',
    course_hours SMALLINT UNSIGNED NOT NULL COMMENT '课时（必须大于0）',
    course_type ENUM('COMPULSORY', 'ELECTIVE') NOT NULL COMMENT '课程类型（必修/选修）',
    exam_type ENUM('CLOSED_BOOK', 'OPEN_BOOK', 'REPORT', 'PRACTICAL') NOT NULL COMMENT '考试类型',
    created_by INT NOT NULL COMMENT '创建人ID（外键）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_course_name (course_name),
    INDEX idx_department_id (department_id),
    INDEX idx_course_type (course_type),
    CONSTRAINT chk_credit CHECK (credit > 0),
    CONSTRAINT chk_course_hours CHECK (course_hours > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '课程表-存储课程基本信息';

-- 学期表（添加选课时间窗口）
CREATE TABLE semesters (
    semester_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '学期ID（主键）',
    academic_year VARCHAR(9) NOT NULL COMMENT '学年（如2023-2024）',
    semester_name VARCHAR(20) NOT NULL COMMENT '学期名称',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    course_selection_start DATE NOT NULL COMMENT '选课开始日期',
    course_selection_end DATE NOT NULL COMMENT '选课结束日期',
    grade_entry_start DATE NOT NULL COMMENT '成绩录入开始日期',
    grade_entry_end DATE NOT NULL COMMENT '成绩录入结束日期',
    is_current TINYINT(1) DEFAULT 0 COMMENT '是否当前学期（0-否，1-是）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_academic_year (academic_year),
    INDEX idx_is_current (is_current),
    INDEX idx_dates (start_date, end_date),
    CONSTRAINT chk_end_date CHECK (end_date > start_date),
    CONSTRAINT chk_selection_dates CHECK (course_selection_end > course_selection_start),
    CONSTRAINT chk_grade_dates CHECK (grade_entry_end > grade_entry_start),
    CONSTRAINT chk_is_current CHECK (is_current IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '学期表-定义学期信息及时间窗口';

-- 教师授课表（修正外键引用，添加唯一约束）
CREATE TABLE teaching_assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '授课ID（主键）',
    teacher_id INT NOT NULL COMMENT '教师ID（外键）',
    course_id INT NOT NULL COMMENT '课程ID（外键）',
    semester_id INT NOT NULL COMMENT '学期ID（外键）',
    classroom VARCHAR(50) COMMENT '教室',
    schedule_info JSON COMMENT '上课时间安排（JSON格式存储详细时间）',
    max_students INT NOT NULL DEFAULT 50 COMMENT '最大学生数',
    current_students INT NOT NULL DEFAULT 0 COMMENT '当前学生数',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (semester_id) REFERENCES semesters(semester_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uk_teacher_course_semester (teacher_id, course_id, semester_id),
    INDEX idx_teacher_course (teacher_id, course_id),
    INDEX idx_semester_course (semester_id, course_id),
    INDEX idx_teacher_semester (teacher_id, semester_id),
    CONSTRAINT chk_max_students CHECK (max_students > 0),
    CONSTRAINT chk_current_students CHECK (current_students >= 0),
    CONSTRAINT chk_student_capacity CHECK (current_students <= max_students)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '教师授课表-记录教师授课安排';

-- 学生选课表（添加时间窗口检查）
CREATE TABLE course_selections (
    selection_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '选课ID（主键）',
    student_id INT NOT NULL COMMENT '学生ID（外键）',
    assignment_id INT NOT NULL COMMENT '授课ID（外键）',
    selection_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    status ENUM('SELECTED', 'DROPPED', 'COMPLETED') DEFAULT 'SELECTED' COMMENT '选课状态',
    dropped_time TIMESTAMP NULL COMMENT '退课时间',
    completed_time TIMESTAMP NULL COMMENT '完成时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (assignment_id) REFERENCES teaching_assignments(assignment_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uk_student_assignment_active (student_id, assignment_id, status),
    INDEX idx_student_id (student_id),
    INDEX idx_assignment_id (assignment_id),
    INDEX idx_status (status),
    INDEX idx_selection_time (selection_time),
    CONSTRAINT chk_status_flow CHECK (
        (status = 'SELECTED' AND dropped_time IS NULL AND completed_time IS NULL) OR
        (status = 'DROPPED' AND dropped_time IS NOT NULL) OR
        (status = 'COMPLETED' AND completed_time IS NOT NULL)
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '学生选课表-记录学生选课信息';

-- 成绩表（添加时间窗口和逻辑检查）
CREATE TABLE course_grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩ID（主键）',
    student_id INT NOT NULL COMMENT '学生ID（外键）',
    assignment_id INT NOT NULL COMMENT '授课ID（外键）',
    usual_score DECIMAL(5,2) COMMENT '平时成绩（0-100）',
    final_grade DECIMAL(5,2) COMMENT '期末成绩（0-100）',
    total_grade DECIMAL(5,2) GENERATED ALWAYS AS (
        CASE 
            WHEN usual_score IS NULL AND final_grade IS NULL THEN NULL
            ELSE COALESCE(usual_score * 0.3, 0) + COALESCE(final_grade * 0.7, 0)
        END
    ) STORED COMMENT '总评成绩（计算列）',
    gpa_grade DECIMAL(3,2) COMMENT '绩点（0-4.0）',
    review_status_id INT NOT NULL DEFAULT 1 COMMENT '审核状态（外键）',
    review_time TIMESTAMP NULL COMMENT '审核时间',
    reviewer_id INT COMMENT '审核人ID（外键）',
    review_comment TEXT COMMENT '审核意见',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (assignment_id) REFERENCES teaching_assignments(assignment_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (review_status_id) REFERENCES grade_review_statuses(status_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    UNIQUE KEY uk_student_assignment_grade (student_id, assignment_id),
    INDEX idx_student_grade (student_id),
    INDEX idx_assignment_grade (assignment_id),
    INDEX idx_review_status (review_status_id),
    INDEX idx_grades_student_semester (student_id, assignment_id),
    CONSTRAINT chk_usual_score_range CHECK (usual_score IS NULL OR (usual_score >= 0 AND usual_score <= 100)),
    CONSTRAINT chk_final_grade_range CHECK (final_grade IS NULL OR (final_grade >= 0 AND final_grade <= 100)),
    CONSTRAINT chk_gpa_grade_range CHECK (gpa_grade IS NULL OR (gpa_grade >= 0 AND gpa_grade <= 4.0)),
    CONSTRAINT chk_grade_consistency CHECK (
        (usual_score IS NULL AND final_grade IS NULL) OR
        (usual_score IS NOT NULL AND final_grade IS NOT NULL)
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '成绩表-记录学生课程成绩';

-- 课程先修关系表
CREATE TABLE course_prerequisites (
    course_id INT NOT NULL COMMENT '课程ID（外键）',
    prerequisite_course_id INT NOT NULL COMMENT '先修课程ID（外键）',
    is_mandatory BOOLEAN DEFAULT TRUE COMMENT '是否强制先修',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (course_id, prerequisite_course_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (prerequisite_course_id) REFERENCES courses(course_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '课程先修关系表-定义课程先修关系';

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
    FOREIGN KEY (category_id) REFERENCES book_categories(category_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_book_title (book_title),
    INDEX idx_author (author),
    INDEX idx_category_id (category_id),
    INDEX idx_available_copies (available_copies),
    CONSTRAINT chk_publish_year CHECK (publish_year IS NULL OR publish_year > 1800),
    CONSTRAINT chk_total_copies CHECK (total_copies > 0),
    CONSTRAINT chk_available_copies CHECK (available_copies >= 0),
    CONSTRAINT chk_available_vs_total CHECK (available_copies <= total_copies)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '图书表-记录图书馆藏书信息';

-- 图书预约表
CREATE TABLE book_reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '预约记录ID（主键）',
    user_id INT NOT NULL COMMENT '预约用户ID（外键）',
    book_id INT NOT NULL COMMENT '图书ID（外键）',
    reservation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
    expiry_time TIMESTAMP NOT NULL COMMENT '预约过期时间',
    status ENUM('PENDING', 'FULFILLED', 'CANCELLED', 'EXPIRED') DEFAULT 'PENDING' COMMENT '预约状态',
    fulfilled_time TIMESTAMP NULL COMMENT '预约实现时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uk_pending_reservation (user_id, book_id, status),
    INDEX idx_user_id (user_id),
    INDEX idx_book_id (book_id),
    INDEX idx_status (status),
    INDEX idx_expiry_time (expiry_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '图书预约表-记录图书预约信息';

-- 图书借阅表
CREATE TABLE book_borrowings (
    borrowing_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '借阅记录ID（主键）',
    user_id INT NOT NULL COMMENT '借阅用户ID（外键）',
    book_id INT NOT NULL COMMENT '图书ID（外键）',
    borrow_date DATE NOT NULL COMMENT '借阅日期',
    due_date DATE NOT NULL COMMENT '应归还日期',
    return_date DATE COMMENT '实际归还日期',
    fine DECIMAL(10,2) DEFAULT 0 COMMENT '罚款金额',
    status_id INT NOT NULL DEFAULT 1 COMMENT '借阅状态（外键）',
    renew_count TINYINT DEFAULT 0 COMMENT '续借次数',
    damage_status ENUM('GOOD', 'MINOR_DAMAGE', 'MAJOR_DAMAGE', 'LOST') DEFAULT 'GOOD' COMMENT '损坏状态',
    compensation DECIMAL(10,2) DEFAULT 0 COMMENT '赔偿金额',
    created_by INT NOT NULL COMMENT '操作人ID（外键）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (status_id) REFERENCES book_borrow_statuses(status_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_book_id (book_id),
    INDEX idx_due_date (due_date),
    INDEX idx_status_id (status_id),
    INDEX idx_return_date (return_date),
    INDEX idx_borrowings_user_status (user_id, status_id),
    CONSTRAINT chk_due_date CHECK (due_date > borrow_date),
    CONSTRAINT chk_return_date CHECK (return_date IS NULL OR return_date >= borrow_date),
    CONSTRAINT chk_fine CHECK (fine >= 0),
    CONSTRAINT chk_renew_count CHECK (renew_count BETWEEN 0 AND 3),
    CONSTRAINT chk_compensation CHECK (compensation >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '图书借阅表-记录图书借阅信息';

-- 数据变更日志表
CREATE TABLE id_changes (
    change_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '变更记录ID（主键）',
    table_name VARCHAR(50) NOT NULL COMMENT '变更的表名',
    record_id INT NOT NULL COMMENT '记录ID',
    old_id VARCHAR(50) NOT NULL COMMENT '旧ID',
    new_id VARCHAR(50) NOT NULL COMMENT '新ID',
    changed_by INT NOT NULL COMMENT '操作人ID（外键）',
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '变更时间',
    reason VARCHAR(255) NOT NULL COMMENT '变更原因',
    FOREIGN KEY (changed_by) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_table_record (table_name, record_id),
    INDEX idx_changed_at (changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = 'ID变更表-记录重要ID变更历史';

-- 系统配置表
CREATE TABLE system_config (
    config_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID（主键）',
    config_key VARCHAR(50) UNIQUE NOT NULL COMMENT '配置键（全局唯一）',
    config_value VARCHAR(500) NOT NULL COMMENT '配置值',
    config_type ENUM('STRING', 'NUMBER', 'BOOLEAN', 'JSON') NOT NULL DEFAULT 'STRING' COMMENT '配置类型',
    description VARCHAR(255) COMMENT '配置描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '系统配置表-存储系统配置参数';

-- 系统操作日志表
CREATE TABLE system_audit_log (
    log_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID（主键）',
    user_id INT NOT NULL COMMENT '操作用户ID（外键）',
    action_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    table_name VARCHAR(50) COMMENT '操作表名',
    record_id INT COMMENT '记录ID',
    old_values JSON COMMENT '旧值',
    new_values JSON COMMENT '新值',
    ip_address VARCHAR(50) COMMENT '操作IP',
    user_agent VARCHAR(500) COMMENT '用户代理',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_action_type (action_type),
    INDEX idx_created_at (created_at),
    INDEX idx_table_record (table_name, record_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '系统操作日志表-记录重要操作日志';

-- 选课时间冲突检查表
CREATE TABLE course_schedule_conflicts (
    conflict_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '冲突ID（主键）',
    student_id INT NOT NULL COMMENT '学生ID（外键）',
    assignment_id1 INT NOT NULL COMMENT '课程安排1ID（外键）',
    assignment_id2 INT NOT NULL COMMENT '课程安排2ID（外键）',
    conflict_type ENUM('TIME', 'PREREQUISITE', 'CAPACITY') NOT NULL COMMENT '冲突类型',
    conflict_details JSON COMMENT '冲突详情',
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
    resolved_at TIMESTAMP NULL COMMENT '解决时间',
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (assignment_id1) REFERENCES teaching_assignments(assignment_id) ON DELETE CASCADE,
    FOREIGN KEY (assignment_id2) REFERENCES teaching_assignments(assignment_id) ON DELETE CASCADE,
    INDEX idx_student_id (student_id),
    INDEX idx_detected_at (detected_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '选课冲突记录表-记录选课冲突信息';

-- 待办事项表
CREATE TABLE todos (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '待办事项ID，主键，自增',
    user_id INT NOT NULL COMMENT '用户ID，关联用户表',
    text VARCHAR(500) NOT NULL COMMENT '待办事项内容',
    completed BOOLEAN DEFAULT FALSE COMMENT '完成状态',
    due_date DATE COMMENT '截止日期',
    important BOOLEAN DEFAULT FALSE COMMENT '是否重要',
    category VARCHAR(50) COMMENT '分类',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_completed (completed),
    INDEX idx_due_date (due_date),
    INDEX idx_important (important)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '待办事项表，存储用户的待办事项信息';

-- 公告表
CREATE TABLE announcements (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID，主键，自增',
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    publish_date DATE NOT NULL COMMENT '发布日期',
    expiry_date DATE COMMENT '过期日期',
    department_id INT COMMENT '发布部门ID（外键）',
    announcement_type ENUM('IMPORTANT', 'NOTICE', 'ACTIVITY', 'INFO') DEFAULT 'NOTICE' COMMENT '公告类型',
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM' COMMENT '优先级',
    created_by INT NOT NULL COMMENT '发布人ID（外键）',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_publish_date (publish_date),
    INDEX idx_type (announcement_type),
    INDEX idx_priority (priority),
    INDEX idx_department_id (department_id),
    CONSTRAINT chk_expiry_date CHECK (expiry_date IS NULL OR expiry_date >= publish_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '公告表，存储系统公告和通知信息';

-- 用户地址表（新增）
CREATE TABLE user_addresses (
    address_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID（主键）',
    user_id INT NOT NULL COMMENT '用户ID（外键）',
    address_type ENUM('HOME', 'SCHOOL', 'WORK') DEFAULT 'HOME' COMMENT '地址类型',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    detail_address VARCHAR(255) COMMENT '详细地址',
    is_primary BOOLEAN DEFAULT FALSE COMMENT '是否主要地址',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_address_type (address_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '用户地址表-存储用户地址信息';

/*
视图定义
*/

-- 学生学术统计视图
CREATE OR REPLACE VIEW student_academic_stats AS
SELECT
    s.student_id,
    s.student_no,
    u.username,
    d.department_name,
    m.major_name,
    ci.class_name,
    ss.status_name AS student_status,
    s.enrollment_date,
    s.education_years
FROM
    students s
        JOIN
    users u ON s.user_id = u.user_id
        JOIN
    departments d ON s.department_id = d.department_id
        JOIN
    majors m ON s.major_id = m.major_id
        JOIN
    class_info ci ON s.class_id = ci.class_id
        JOIN
    student_statuses ss ON s.status_id = ss.status_id;

-- 课程统计视图
CREATE OR REPLACE VIEW course_statistics AS
SELECT
    c.course_id,
    c.course_code,
    c.course_name,
    d.department_name,
    s.semester_id,
    s.academic_year,
    s.semester_name,
    COUNT(DISTINCT cg.student_id) AS total_students,
    AVG(cg.final_grade) AS avg_final_grade,
    AVG(cg.total_grade) AS avg_total_grade,
    MIN(cg.final_grade) AS min_final_grade,
    MAX(cg.final_grade) AS max_final_grade,
    COUNT(CASE WHEN cg.final_grade >= 60 THEN 1 END) AS passed_students,
    COUNT(CASE WHEN cg.final_grade < 60 THEN 1 END) AS failed_students
FROM
    courses c
        JOIN
    departments d ON c.department_id = d.department_id
        JOIN
    teaching_assignments ta ON c.course_id = ta.course_id
        JOIN
    semesters s ON ta.semester_id = s.semester_id
        LEFT JOIN
    course_grades cg ON ta.assignment_id = cg.assignment_id
WHERE
    cg.review_status_id = 2  -- 仅包含已审核通过的成绩
GROUP BY
    c.course_id, c.course_code, c.course_name, d.department_name,
    s.semester_id, s.academic_year, s.semester_name;

-- 教师授课统计视图
CREATE OR REPLACE VIEW teacher_course_stats AS
SELECT
    t.teacher_id,
    t.teacher_no,
    u.username,
    d.department_name,
    tt.title_name,
    ts.status_name AS teacher_status,
    COUNT(ta.assignment_id) AS total_assignments,
    SUM(ta.current_students) AS total_students,
    AVG(ta.current_students) AS avg_students_per_course,
    MIN(ta.current_students) AS min_students,
    MAX(ta.current_students) AS max_students
FROM
    teachers t
        JOIN
    users u ON t.user_id = u.user_id
        JOIN
    departments d ON t.department_id = d.department_id
        JOIN
    teacher_titles tt ON t.title_id = tt.title_id
        JOIN
    teacher_statuses ts ON t.status_id = ts.status_id
        LEFT JOIN
    teaching_assignments ta ON t.teacher_id = ta.teacher_id
GROUP BY
    t.teacher_id, t.teacher_no, u.username, d.department_name,
    tt.title_name, ts.status_name;

-- 图书借阅统计视图
CREATE OR REPLACE VIEW book_borrowing_stats AS
SELECT
    b.book_id,
    b.isbn,
    b.book_title,
    b.author,
    bc.category_name,
    b.total_copies,
    b.available_copies,
    COUNT(bb.borrowing_id) AS total_borrowings,
    COUNT(CASE WHEN bb.return_date IS NULL THEN 1 END) AS current_borrowings,
    AVG(DATEDIFF(bb.return_date, bb.borrow_date)) AS avg_borrow_days
FROM
    books b
        JOIN
    book_categories bc ON b.category_id = bc.category_id
        LEFT JOIN
    book_borrowings bb ON b.book_id = bb.book_id
GROUP BY
    b.book_id, b.isbn, b.book_title, b.author, bc.category_name,
    b.total_copies, b.available_copies;

/*
初始化数据
*/

-- 初始化系统配置
INSERT INTO system_config (config_key, config_value, config_type, description) VALUES
    ('student_id_format', 'YYYY{dept}{major}####', 'STRING', '学号格式：入学年份+学院代码+专业代码+4位序号'),
    ('teacher_id_format', 'YY{dept}####', 'STRING', '教师编号格式：入职年份后两位+学院代码+4位序号'),
    ('max_books_per_student', '5', 'NUMBER', '每个学生最多可借图书数量'),
    ('max_borrow_days', '30', 'NUMBER', '图书最长借阅天数'),
    ('max_renew_times', '2', 'NUMBER', '图书最大续借次数'),
    ('fine_per_day', '0.5', 'NUMBER', '图书逾期每日罚款金额'),
    ('max_fine_amount', '50', 'NUMBER', '单本书最高罚款金额'),
    ('graduation_credits', '160', 'NUMBER', '毕业要求总学分'),
    ('min_graduation_gpa', '2.0', 'NUMBER', '毕业最低GPA要求'),
    ('max_course_selection', '8', 'NUMBER', '学生每学期最多选课数量'),
    ('auto_approve_threshold', '85', 'NUMBER', '成绩自动审核通过的分数阈值'),
    ('course_drop_deadline_weeks', '2', 'NUMBER', '退课截止周数（开课后）'),
    ('reservation_expiry_days', '3', 'NUMBER', '图书预约过期天数');

-- 初始化角色数据
INSERT INTO roles (role_name, description) VALUES
    ('ADMIN', '系统管理员'),
    ('TEACHER', '教师'),
    ('STUDENT', '学生');

-- 初始化权限数据
INSERT INTO permissions (permission_name, description, module) VALUES
-- 用户管理模块
('USER_CREATE', '创建用户', 'USER_MANAGEMENT'),
('USER_EDIT', '编辑用户', 'USER_MANAGEMENT'),
('USER_DELETE', '删除用户', 'USER_MANAGEMENT'),
('USER_VIEW', '查看用户', 'USER_MANAGEMENT'),
-- 权限管理模块
('ROLE_CREATE', '创建角色', 'PERMISSION_MANAGEMENT'),
('ROLE_EDIT', '编辑角色', 'PERMISSION_MANAGEMENT'),
('ROLE_DELETE', '删除角色', 'PERMISSION_MANAGEMENT'),
('PERMISSION_ASSIGN', '分配权限', 'PERMISSION_MANAGEMENT'),
-- 教学管理模块（课程）
('COURSE_CREATE', '创建课程', 'TEACHING_MANAGEMENT'),
('COURSE_EDIT', '编辑课程', 'TEACHING_MANAGEMENT'),
('COURSE_DELETE', '删除课程', 'TEACHING_MANAGEMENT'),
('COURSE_VIEW', '查看课程', 'TEACHING_MANAGEMENT'),
-- 教学管理模块（成绩）
('GRADE_INPUT', '录入成绩', 'TEACHING_MANAGEMENT'),
('GRADE_EDIT', '编辑成绩', 'TEACHING_MANAGEMENT'),
('GRADE_REVIEW', '审核成绩', 'TEACHING_MANAGEMENT'),
('GRADE_VIEW', '查看成绩', 'TEACHING_MANAGEMENT'),
-- 图书馆管理模块（图书）
('BOOK_CREATE', '新增图书', 'LIBRARY_MANAGEMENT'),
('BOOK_EDIT', '编辑图书', 'LIBRARY_MANAGEMENT'),
('BOOK_DELETE', '删除图书', 'LIBRARY_MANAGEMENT'),
('BOOK_VIEW', '查看图书', 'LIBRARY_MANAGEMENT'),
-- 图书馆管理模块（借阅）
('BOOK_BORROW', '借阅图书', 'LIBRARY_MANAGEMENT'),
('BOOK_RETURN', '归还图书', 'LIBRARY_MANAGEMENT'),
('BOOK_RENEW', '续借图书', 'LIBRARY_MANAGEMENT'),
-- 系统分析模块
('STATISTICS_VIEW', '查看统计数据', 'SYSTEM_ANALYTICS'),
-- 系统管理模块
('SYSTEM_CONFIG', '系统配置管理', 'SYSTEM_ADMIN');

-- 初始化角色-权限映射
INSERT INTO role_permissions (role_id, permission_id) VALUES
-- 管理员（1）：所有权限
(1,1), (1,2), (1,3), (1,4), (1,5), (1,6), (1,7), (1,8),
(1,9), (1,10), (1,11), (1,12), (1,13), (1,14), (1,15), (1,16),
(1,17), (1,18), (1,19), (1,20), (1,21), (1,22), (1,23), (1,24),
-- 教师（2）：课程+成绩+统计权限
(2,12), (2,13), (2,14), (2,15), (2,16), (2,20), (2,23),
-- 学生（3）：查看课程+成绩+借阅权限
(3,12), (3,16), (3,20), (3,21), (3,22), (3,23);

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
    ('讲师', 2),
    ('副教授', 2),
    ('教授', 3);

-- 初始化学院表
INSERT INTO departments (department_name, department_code) VALUES
    ('计算机科学与技术学院', 'CS'),
    ('电子工程学院', 'EE'),
    ('机械工程学院', 'ME'),
    ('管理学院', 'BM'),
    ('外国语学院', 'FL');

-- 初始化专业表
INSERT INTO majors (major_name, department_id, major_code, required_credits) VALUES
    ('计算机科学与技术', 1, 'CS01', 160),
    ('软件工程', 1, 'CS02', 165),
    ('电子信息工程', 2, 'EE01', 155),
    ('机械设计制造', 3, 'ME01', 158),
    ('工商管理', 4, 'BM01', 150),
    ('英语', 5, 'FL01', 145);

-- 初始化班级表
INSERT INTO class_info (class_name, major_id, enrollment_year, classroom) VALUES
    ('计算机2021-1班', 1, 2021, '教学楼A201'),
    ('计算机2021-2班', 1, 2021, '教学楼A202'),
    ('软件2021-1班', 2, 2021, '教学楼B101'),
    ('电子2021-1班', 3, 2021, '教学楼C301');

-- 初始化图书分类表
INSERT INTO book_categories (category_name, parent_id) VALUES
    ('计算机科学', NULL),
    ('编程语言', 1),
    ('数据库', 1),
    ('文学', NULL),
    ('小说', 4),
    ('历史', NULL);

-- 初始化学期表
INSERT INTO semesters (academic_year, semester_name, start_date, end_date, course_selection_start, course_selection_end, grade_entry_start, grade_entry_end, is_current) VALUES
    ('2023-2024', '第一学期', '2023-09-01', '2024-01-20', '2023-08-20', '2023-09-10', '2024-01-10', '2024-01-30', 0),
    ('2023-2024', '第二学期', '2024-02-26', '2024-07-15', '2024-02-15', '2024-03-10', '2024-07-01', '2024-07-20', 1);

-- 初始化用户表（管理员）
INSERT INTO users (username, password_hash, email, phone, gender, status, created_by) VALUES
    ('ADMIN01', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin@lucky-sms.com', '13300000000', 'M', 'ACTIVE', 1),
    ('TEACHER01', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'teacher1@lucky-sms.com', '13300000001', 'M', 'ACTIVE', 1),
    ('STUDENT01', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'student1@lucky-sms.com', '13800000002', 'M', 'ACTIVE', 1),
    ('STUDENT02', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'student2@lucky-sms.com', '13800000003', 'F', 'ACTIVE', 1);

-- 为用户分配角色
INSERT INTO user_roles (user_id, role_id, assigned_by) VALUES
    (1, 1, 1),  -- ADMIN01 作为管理员
    (2, 2, 1),  -- TEACHER01 作为教师
    (3, 3, 1),  -- STUDENT01 作为学生
    (4, 3, 1);  -- STUDENT02 作为学生

-- 初始化教师表
INSERT INTO teachers (user_id, department_id, title_id, hire_date, office_location, teacher_no, status_id, created_by) VALUES
    (2, 1, 4, '2020-09-01', '计算机学院A301', 'T2020001', 1, 1);

-- 设置计算机学院院长
UPDATE departments SET dean_id = 1 WHERE department_id = 1;

-- 设置班级班主任
UPDATE class_info SET class_advisor_id = 1 WHERE class_id IN (1, 2);

-- 初始化学生表
INSERT INTO students (user_id, department_id, major_id, class_id, enrollment_date, education_years, student_no, status_id, emergency_contact, emergency_phone, created_by) VALUES
    (3, 1, 1, 1, '2021-09-01', 4, '2021CS0001', 1, '张爸爸', '13800000010', 1),
    (4, 1, 1, 2, '2021-09-01', 4, '2021CS0002', 1, '李妈妈', '13800000011', 1);

-- 初始化课程表
INSERT INTO courses (course_code, course_name, course_description, department_id, credit, course_hours, course_type, exam_type, created_by) VALUES
    ('CS101', '计算机基础', '计算机科学基础课程', 1, 3.0, 48, 'COMPULSORY', 'CLOSED_BOOK', 1),
    ('CS102', '数据结构', '数据结构与算法', 1, 4.0, 64, 'COMPULSORY', 'CLOSED_BOOK', 1),
    ('CS201', 'Java程序设计', 'Java语言程序设计', 1, 3.0, 48, 'COMPULSORY', 'PRACTICAL', 1),
    ('CS301', '数据库系统', '数据库原理与应用', 1, 3.0, 48, 'COMPULSORY', 'OPEN_BOOK', 1);

-- 初始化课程先修关系
INSERT INTO course_prerequisites (course_id, prerequisite_course_id, is_mandatory) VALUES
    (2, 1, TRUE),   -- 数据结构需要计算机基础
    (3, 2, TRUE),   -- Java程序设计需要数据结构
    (4, 2, TRUE);   -- 数据库系统需要数据结构

-- 初始化教师授课表
INSERT INTO teaching_assignments (teacher_id, course_id, semester_id, classroom, schedule_info, max_students, current_students) VALUES
    (1, 1, 2, '教学楼A101', '{"day": "MONDAY", "startTime": "08:00", "endTime": "09:40", "weeks": [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16]}', 50, 0),
    (1, 2, 2, '教学楼A102', '{"day": "WEDNESDAY", "startTime": "10:00", "endTime": "11:40", "weeks": [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16]}', 40, 0);

-- 初始化学生选课表
INSERT INTO course_selections (student_id, assignment_id, status) VALUES
    (1, 1, 'SELECTED'),
    (1, 2, 'SELECTED'),
    (2, 1, 'SELECTED');

-- 更新课程当前人数
UPDATE teaching_assignments SET current_students = 2 WHERE assignment_id = 1;
UPDATE teaching_assignments SET current_students = 1 WHERE assignment_id = 2;

-- 初始化图书表
INSERT INTO books (isbn, book_title, author, publisher, publish_year, category_id, location, total_copies, available_copies, created_by) VALUES
    ('9787111213826', 'Java编程思想', 'Bruce Eckel', '机械工业出版社', 2020, 2, '图书馆A区101', 5, 5, 1),
    ('9787121201212', '数据结构与算法', '严蔚敏', '清华大学出版社', 2019, 2, '图书馆A区102', 3, 3, 1),
    ('9787302274624', '数据库系统概念', 'Abraham Silberschatz', '机械工业出版社', 2021, 3, '图书馆B区201', 4, 4, 1);

-- 初始化待办事项表数据
INSERT INTO todos (user_id, text, completed, due_date, important, category) VALUES
    (3, '完成学生个人信息核对', FALSE, '2024-09-24', TRUE, '学工'),
    (3, '查收新生开学必读须知', FALSE, '2024-06-25', FALSE, '通知'),
    (3, '完成数据结构作业第二章', FALSE, '2024-06-25', FALSE, '作业');

-- 初始化公告表数据
INSERT INTO announcements (title, content, publish_date, expiry_date, department_id, announcement_type, priority, created_by) VALUES
    ('期末考试安排通知', '各位同学请注意，期末考试将于下周一开始，请做好充分准备。考试时间为上午9:00-11:00，地点在各班教室。', '2024-06-15', '2024-07-01', 1, 'IMPORTANT', 'HIGH', 1),
    ('图书馆开放时间调整', '由于系统维护，图书馆本周三下午将临时关闭，请同学们合理安排借阅时间。', '2024-06-14', '2024-06-21', 1, 'NOTICE', 'MEDIUM', 1),
    ('校园文化节活动预告', '我校将于下月举办校园文化节，欢迎各位同学积极参与各项活动。具体活动安排请关注后续通知。', '2024-06-13', '2024-07-13', 1, 'ACTIVITY', 'MEDIUM', 1),
    ('校园招聘信息', '多家知名企业将于下周来校招聘，欢迎大四同学参加。请准备好个人简历和相关材料。', '2024-06-12', '2024-06-30', 1, 'INFO', 'LOW', 1);

-- 修复用户表的自引用外键约束（在表创建后添加，避免循环依赖）
ALTER TABLE users
    ADD CONSTRAINT fk_users_created_by
        FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_users_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;

-- 开启外键检查
SET FOREIGN_KEY_CHECKS = 1;