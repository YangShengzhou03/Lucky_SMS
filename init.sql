DROP DATABASE IF EXISTS lucky_sms;
CREATE DATABASE lucky_sms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lucky_sms;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS status_types (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL,
    status_category VARCHAR(50) NOT NULL,
    description TEXT,
    color_code VARCHAR(7),
    sort_order INT DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_status_category (status_category),
    INDEX idx_status_active (is_active)
);

CREATE TABLE IF NOT EXISTS user_types (
    user_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_code VARCHAR(20) UNIQUE NOT NULL,
    type_name VARCHAR(50) NOT NULL,
    description TEXT,
    permissions JSON,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS genders (
    gender_id INT PRIMARY KEY AUTO_INCREMENT,
    gender_code VARCHAR(10) UNIQUE NOT NULL,
    gender_name VARCHAR(20) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    avatar VARCHAR(200),
    real_name VARCHAR(50),
    gender_id INT,
    birthday DATE,
    address VARCHAR(200),
    user_type_id INT NOT NULL,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_email (email),
    INDEX idx_user_type (user_type_id),
    INDEX idx_user_status (status_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id),
    FOREIGN KEY (user_type_id) REFERENCES user_types(user_type_id),
    FOREIGN KEY (gender_id) REFERENCES genders(gender_id)
);

CREATE TABLE IF NOT EXISTS departments (
    department_id INT PRIMARY KEY AUTO_INCREMENT,
    department_code VARCHAR(20) UNIQUE NOT NULL,
    department_name VARCHAR(100) NOT NULL,
    department_type_id INT,
    parent_department_id INT,
    description TEXT,
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(200),
    website VARCHAR(100),
    dean_user_id INT,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_department_code (department_code),
    INDEX idx_parent_department (parent_department_id),
    INDEX idx_department_status (status_id),
    INDEX idx_dean_user (dean_user_id),
    FOREIGN KEY (parent_department_id) REFERENCES departments(department_id),
    FOREIGN KEY (dean_user_id) REFERENCES users(user_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS majors (
    major_id INT PRIMARY KEY AUTO_INCREMENT,
    major_code VARCHAR(20) UNIQUE NOT NULL,
    major_name VARCHAR(100) NOT NULL,
    department_id INT NOT NULL,
    major_type_id INT,
    degree_type_id INT,
    duration_years INT DEFAULT 4,
    description TEXT,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_major_code (major_code),
    INDEX idx_department (department_id),
    INDEX idx_major_status (status_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS class_info (
    class_id INT PRIMARY KEY AUTO_INCREMENT,
    class_code VARCHAR(20) UNIQUE NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    major_id INT NOT NULL,
    grade_level INT NOT NULL,
    class_number INT NOT NULL,
    academic_year VARCHAR(9),
    head_teacher_user_id INT,
    counselor_user_id INT,
    student_count INT DEFAULT 0,
    max_students INT DEFAULT 50,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_class_code (class_code),
    INDEX idx_major (major_id),
    INDEX idx_grade_level (grade_level),
    INDEX idx_head_teacher (head_teacher_user_id),
    INDEX idx_counselor (counselor_user_id),
    INDEX idx_class_status (status_id),
    FOREIGN KEY (major_id) REFERENCES majors(major_id),
    FOREIGN KEY (head_teacher_user_id) REFERENCES users(user_id),
    FOREIGN KEY (counselor_user_id) REFERENCES users(user_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS teachers (
    teacher_id INT PRIMARY KEY AUTO_INCREMENT,
    teacher_no VARCHAR(20) UNIQUE NOT NULL,
    user_id INT NOT NULL,
    department_id INT NOT NULL,
    title_id INT,
    hire_date DATE,
    office_location VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    research_direction TEXT,
    education_background VARCHAR(200),
    work_experience TEXT,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_teacher_no (teacher_no),
    INDEX idx_user_id (user_id),
    INDEX idx_department (department_id),
    INDEX idx_title (title_id),
    INDEX idx_teacher_status (status_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    student_no VARCHAR(20) UNIQUE NOT NULL,
    user_id INT NOT NULL,
    department_id INT NOT NULL,
    major_id INT NOT NULL,
    class_id INT,
    enrollment_date DATE NOT NULL,
    graduation_date DATE,
    education_years INT DEFAULT 4,
    education_level_id INT,
    status_id INT DEFAULT 1,
    emergency_contact VARCHAR(50),
    emergency_phone VARCHAR(20),
    parent_name VARCHAR(50),
    parent_phone VARCHAR(20),
    home_address VARCHAR(200),
    high_school VARCHAR(100),
    entrance_score DECIMAL(5,2),
    scholarship_info TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_student_no (student_no),
    INDEX idx_user_id (user_id),
    INDEX idx_department (department_id),
    INDEX idx_major (major_id),
    INDEX idx_class (class_id),
    INDEX idx_student_status (status_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    FOREIGN KEY (major_id) REFERENCES majors(major_id),
    FOREIGN KEY (class_id) REFERENCES class_info(class_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    course_type_id INT NOT NULL,
    credit DECIMAL(3,1) NOT NULL,
    total_hours INT,
    theory_hours INT,
    practice_hours INT,
    department_id INT NOT NULL,
    major_id INT,
    description TEXT,
    syllabus TEXT,
    textbook VARCHAR(200),
    prerequisites TEXT,
    is_compulsory BOOLEAN DEFAULT TRUE,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_course_code (course_code),
    INDEX idx_course_name (course_name),
    INDEX idx_course_type (course_type_id),
    INDEX idx_department (department_id),
    INDEX idx_major (major_id),
    INDEX idx_course_status (status_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id),
    FOREIGN KEY (major_id) REFERENCES majors(major_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS semesters (
    semester_id INT PRIMARY KEY AUTO_INCREMENT,
    semester_name VARCHAR(50) NOT NULL,
    academic_year VARCHAR(9) NOT NULL,
    semester_type_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    course_selection_start DATETIME,
    course_selection_end DATETIME,
    grade_entry_start DATETIME,
    grade_entry_end DATETIME,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_academic_year (academic_year),
    INDEX idx_semester_type (semester_type_id),
    INDEX idx_semester_status (status_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS teaching_assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    teacher_id INT NOT NULL,
    semester_id INT NOT NULL,
    class_time VARCHAR(200),
    classroom VARCHAR(100),
    max_students INT DEFAULT 100,
    current_students INT DEFAULT 0,
    credit_hours INT,
    course_type_id INT,
    assessment_method_id INT,
    status_id INT DEFAULT 1,
    description TEXT,
    requirements TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_course (course_id),
    INDEX idx_teacher (teacher_id),
    INDEX idx_semester (semester_id),
    INDEX idx_classroom (classroom),
    INDEX idx_assignment_status (status_id),
    UNIQUE KEY uk_course_teacher_semester (course_id, teacher_id, semester_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id),
    FOREIGN KEY (semester_id) REFERENCES semesters(semester_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS course_grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    assignment_id INT NOT NULL,
    usual_score DECIMAL(5,2),
    midterm_score DECIMAL(5,2),
    final_score DECIMAL(5,2),
    total_score DECIMAL(5,2),
    gpa_grade DECIMAL(3,2),
    grade_level_id INT,
    make_up_score DECIMAL(5,2),
    retake_score DECIMAL(5,2),
    review_status_id INT DEFAULT 1,
    review_time DATETIME,
    reviewer_id INT,
    review_comment TEXT,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_student (student_id),
    INDEX idx_assignment (assignment_id),
    INDEX idx_grade_level (grade_level_id),
    INDEX idx_review_status (review_status_id),
    INDEX idx_reviewer (reviewer_id),
    INDEX idx_grade_status (status_id),
    UNIQUE KEY uk_student_assignment (student_id, assignment_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (assignment_id) REFERENCES teaching_assignments(assignment_id),
    FOREIGN KEY (reviewer_id) REFERENCES users(user_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS course_selections (
    selection_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    assignment_id INT NOT NULL,
    selection_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    selection_type_id INT DEFAULT 1,
    priority INT DEFAULT 1,
    status_id INT DEFAULT 1,
    drop_time DATETIME,
    drop_reason VARCHAR(200),
    final_grade DECIMAL(5,2),
    attendance_rate DECIMAL(5,2),
    evaluation_score DECIMAL(3,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_student (student_id),
    INDEX idx_assignment (assignment_id),
    INDEX idx_selection_time (selection_time),
    INDEX idx_selection_status (status_id),
    UNIQUE KEY uk_student_assignment (student_id, assignment_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (assignment_id) REFERENCES teaching_assignments(assignment_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS teacher_titles (
    title_id INT PRIMARY KEY AUTO_INCREMENT,
    title_name VARCHAR(50) NOT NULL,
    title_level INT,
    description TEXT,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_title_level (title_level),
    INDEX idx_title_status (status_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id)
);

CREATE TABLE IF NOT EXISTS course_types (
    course_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_code VARCHAR(20) UNIQUE NOT NULL,
    type_name VARCHAR(50) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_course_type_code (type_code),
    INDEX idx_course_type_active (is_active)
);

CREATE TABLE IF NOT EXISTS semester_types (
    semester_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_code VARCHAR(20) UNIQUE NOT NULL,
    type_name VARCHAR(50) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_semester_type_code (type_code),
    INDEX idx_semester_type_active (is_active)
);

CREATE TABLE IF NOT EXISTS selection_types (
    selection_type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_code VARCHAR(20) UNIQUE NOT NULL,
    type_name VARCHAR(50) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_selection_type_code (type_code),
    INDEX idx_selection_type_active (is_active)
);

CREATE TABLE IF NOT EXISTS grade_levels (
    grade_level_id INT PRIMARY KEY AUTO_INCREMENT,
    level_code VARCHAR(10) UNIQUE NOT NULL,
    level_name VARCHAR(20) NOT NULL,
    min_score DECIMAL(5,2) NOT NULL,
    max_score DECIMAL(5,2) NOT NULL,
    gpa_points DECIMAL(3,2) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_grade_level_code (level_code),
    INDEX idx_grade_level_active (is_active)
);

CREATE TABLE IF NOT EXISTS assessment_methods (
    assessment_method_id INT PRIMARY KEY AUTO_INCREMENT,
    method_code VARCHAR(20) UNIQUE NOT NULL,
    method_name VARCHAR(50) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_assessment_method_code (method_code),
    INDEX idx_assessment_method_active (is_active)
);

CREATE TABLE IF NOT EXISTS review_statuses (
    review_status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_code VARCHAR(20) UNIQUE NOT NULL,
    status_name VARCHAR(50) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    
    INDEX idx_review_status_code (status_code),
    INDEX idx_review_status_active (is_active)
);

INSERT IGNORE INTO status_types (status_name, status_category, description, color_code, sort_order) VALUES
('正常', 'user_status', '用户正常状态', '#67C23A', 1),
('禁用', 'user_status', '用户被禁用', '#F56C6C', 2),
('待审核', 'user_status', '用户待审核', '#E6A23C', 3),
('已删除', 'user_status', '用户已删除', '#909399', 4),
('正常', 'course_status', '课程正常状态', '#67C23A', 1),
('已满', 'course_status', '课程人数已满', '#E6A23C', 2),
('已结束', 'course_status', '课程已结束', '#909399', 3),
('已选', 'selection_status', '选课成功', '#67C23A', 1),
('已退', 'selection_status', '已退课', '#F56C6C', 2),
('待确认', 'selection_status', '选课待确认', '#E6A23C', 3),
('优秀', 'grade_level', '优秀成绩', '#67C23A', 1),
('良好', 'grade_level', '良好成绩', '#409EFF', 2),
('中等', 'grade_level', '中等成绩', '#E6A23C', 3),
('及格', 'grade_level', '及格成绩', '#909399', 4),
('不及格', 'grade_level', '不及格成绩', '#F56C6C', 5);

INSERT IGNORE INTO teacher_titles (title_name, title_level, description) VALUES
('教授', 1, '正高级职称'),
('副教授', 2, '副高级职称'),
('讲师', 3, '中级职称'),
('助教', 4, '初级职称'),
('研究员', 1, '正高级研究职称'),
('副研究员', 2, '副高级研究职称');

INSERT IGNORE INTO course_types (type_code, type_name, description, sort_order) VALUES
('THEORY', '理论课', '以理论教学为主的课程', 1),
('PRACTICE', '实践课', '以实践教学为主的课程', 2),
('EXPERIMENT', '实验课', '实验教学课程', 3),
('DESIGN', '设计课', '课程设计类课程', 4),
('THESIS', '论文', '毕业论文/设计', 5),
('INTERNSHIP', '实习', '实习实训课程', 6);

INSERT IGNORE INTO semester_types (type_code, type_name, description, sort_order) VALUES
('SPRING', '春季学期', '春季学期，通常从2月到7月', 1),
('FALL', '秋季学期', '秋季学期，通常从9月到次年1月', 2),
('SUMMER', '夏季学期', '夏季短学期', 3),
('WINTER', '冬季学期', '冬季短学期', 4);

INSERT IGNORE INTO selection_types (type_code, type_name, description, sort_order) VALUES
('NORMAL', '正常选课', '正常选课流程', 1),
('MAKE_UP', '补选', '补选课程', 2),
('RETAKE', '重修选课', '重修课程选择', 3),
('AUDIT', '旁听', '旁听课程选择', 4);

INSERT IGNORE INTO grade_levels (level_code, level_name, min_score, max_score, gpa_points, sort_order) VALUES
('A+', '优秀', 95.00, 100.00, 4.0, 1),
('A', '优秀', 90.00, 94.99, 3.7, 2),
('B+', '良好', 85.00, 89.99, 3.3, 3),
('B', '良好', 80.00, 84.99, 3.0, 4),
('B-', '良好', 75.00, 79.99, 2.7, 5),
('C+', '中等', 70.00, 74.99, 2.3, 6),
('C', '中等', 65.00, 69.99, 2.0, 7),
('C-', '中等', 60.00, 64.99, 1.7, 8),
('D', '及格', 55.00, 59.99, 1.0, 9),
('F', '不及格', 0.00, 54.99, 0.0, 10);

INSERT IGNORE INTO assessment_methods (method_code, method_name, description, sort_order) VALUES
('EXAM', '考试', '闭卷考试', 1),
('OPEN_EXAM', '开卷考试', '开卷考试', 2),
('PAPER', '论文', '课程论文', 3),
('PROJECT', '项目', '课程项目', 4),
('PRESENTATION', '展示', '课堂展示', 5),
('PRACTICAL', '实操', '实际操作考核', 6);

INSERT IGNORE INTO review_statuses (status_code, status_name, description, sort_order) VALUES
('PENDING', '待审核', '成绩待审核', 1),
('APPROVED', '已通过', '成绩审核通过', 2),
('REJECTED', '已拒绝', '成绩审核拒绝', 3),
('MODIFIED', '已修改', '成绩已修改', 4);

INSERT IGNORE INTO genders (gender_code, gender_name, sort_order) VALUES
('MALE', '男', 1),
('FEMALE', '女', 2),
('OTHER', '其他', 3);

INSERT IGNORE INTO user_types (type_code, type_name, description, permissions, sort_order) VALUES
('ADMIN', '管理员', '系统管理员', '{"system": ["read", "write", "delete"], "users": ["read", "write", "delete"], "courses": ["read", "write", "delete"], "grades": ["read", "write", "delete"]}', 1),
('TEACHER', '教师', '教师用户', '{"courses": ["read", "write"], "grades": ["read", "write"], "students": ["read"]}', 2),
('STUDENT', '学生', '学生用户', '{"courses": ["read"], "grades": ["read"], "profile": ["read", "write"]}', 3);

INSERT IGNORE INTO departments (department_code, department_name, description, status_id, created_at, updated_at) VALUES
('CS', '计算机科学与技术学院', '计算机科学与技术学院', 1, NOW(), NOW()),
('MATH', '数学学院', '数学学院', 1, NOW(), NOW()),
('PHYS', '物理学院', '物理学院', 1, NOW(), NOW()),
('CHEM', '化学学院', '化学学院', 1, NOW(), NOW()),
('BIO', '生命科学学院', '生命科学学院', 1, NOW(), NOW());

INSERT IGNORE INTO majors (major_code, major_name, department_id, description, status_id, created_at, updated_at) VALUES
('CS001', '计算机科学与技术', 1, '计算机科学与技术专业', 1, NOW(), NOW()),
('CS002', '软件工程', 1, '软件工程专业', 1, NOW(), NOW()),
('CS003', '网络工程', 1, '网络工程专业', 1, NOW(), NOW()),
('MATH001', '数学与应用数学', 2, '数学与应用数学专业', 1, NOW(), NOW()),
('PHYS001', '物理学', 3, '物理学专业', 1, NOW(), NOW());

INSERT IGNORE INTO semesters (semester_name, academic_year, semester_type_id, start_date, end_date, course_selection_start, course_selection_end, grade_entry_start, grade_entry_end, status_id, created_at, updated_at) VALUES
('2024春季学期', '2023-2024', 1, '2024-02-26', '2024-07-15', '2024-01-15 00:00:00', '2024-02-25 23:59:59', '2024-06-01 00:00:00', '2024-07-15 23:59:59', 1, NOW(), NOW()),
('2024秋季学期', '2024-2025', 2, '2024-09-02', '2025-01-20', '2024-08-01 00:00:00', '2024-09-01 23:59:59', '2024-12-15 00:00:00', '2025-01-20 23:59:59', 1, NOW(), NOW());

INSERT IGNORE INTO users (username, password, phone, email, real_name, gender_id, user_type_id, status_id, created_at, updated_at) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaUKk7h.T0mUO', '13800138000', 'admin@university.edu.cn', '管理员', 1, 1, 1, NOW(), NOW()),
('teacher001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaUKk7h.T0mUO', '13800138001', 'teacher001@university.edu.cn', '张教授', 1, 2, 1, NOW(), NOW()),
('teacher002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaUKk7h.T0mUO', '13800138002', 'teacher002@university.edu.cn', '李教授', 2, 2, 1, NOW(), NOW()),
('student001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaUKk7h.T0mUO', '13800138003', 'student001@university.edu.cn', '王同学', 1, 3, 1, NOW(), NOW()),
('student002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaUKk7h.T0mUO', '13800138004', 'student002@university.edu.cn', '李同学', 2, 3, 1, NOW(), NOW()),
('student003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaUKk7h.T0mUO', '13800138005', 'student003@university.edu.cn', '张同学', 1, 3, 1, NOW(), NOW());

INSERT IGNORE INTO teachers (teacher_no, user_id, department_id, title_id, hire_date, office_location, phone, email, status_id, created_at, updated_at) VALUES
('T001', 2, 1, 1, '2015-09-01', '计算机楼301', '13800138001', 'teacher001@university.edu.cn', 1, NOW(), NOW()),
('T002', 3, 1, 2, '2018-03-15', '计算机楼302', '13800138002', 'teacher002@university.edu.cn', 1, NOW(), NOW());

INSERT IGNORE INTO students (student_no, user_id, department_id, major_id, enrollment_date, education_years, status_id, created_at, updated_at) VALUES
('S001', 4, 1, 1, '2022-09-01', 4, 1, NOW(), NOW()),
('S002', 5, 1, 1, '2022-09-01', 4, 1, NOW(), NOW()),
('S003', 6, 1, 2, '2023-09-01', 4, 1, NOW(), NOW());

INSERT IGNORE INTO courses (course_code, course_name, course_type_id, credit, department_id, major_id, is_compulsory, status_id, created_at, updated_at) VALUES
('CS101', '程序设计基础', 1, 4.0, 1, 1, TRUE, 1, NOW(), NOW()),
('CS102', '数据结构', 1, 3.5, 1, 1, TRUE, 1, NOW(), NOW()),
('CS201', '算法分析', 1, 3.0, 1, 1, TRUE, 1, NOW(), NOW()),
('CS202', '数据库系统', 1, 3.5, 1, 1, TRUE, 1, NOW(), NOW()),
('CS301', '软件工程', 1, 3.0, 1, 2, TRUE, 1, NOW(), NOW()),
('CS302', '计算机网络', 1, 3.0, 1, 1, TRUE, 1, NOW(), NOW());

INSERT IGNORE INTO teaching_assignments (course_id, teacher_id, semester_id, class_time, classroom, max_students, current_students, status_id, created_at, updated_at) VALUES
(1, 1, 1, '周一 8:00-10:00, 周三 8:00-10:00', 'A101', 120, 85, 1, NOW(), NOW()),
(2, 1, 1, '周二 14:00-16:00, 周四 14:00-16:00', 'A102', 100, 78, 1, NOW(), NOW()),
(3, 2, 1, '周一 14:00-16:00, 周五 14:00-16:00', 'A201', 80, 65, 1, NOW(), NOW()),
(4, 2, 1, '周三 10:00-12:00, 周五 10:00-12:00', 'A202', 90, 72, 1, NOW(), NOW()),
(5, 1, 2, '周二 8:00-10:00, 周四 8:00-10:00', 'B101', 60, 45, 1, NOW(), NOW()),
(6, 2, 2, '周一 16:00-18:00, 周三 16:00-18:00', 'B201', 70, 58, 1, NOW(), NOW());

INSERT IGNORE INTO course_selections (student_id, assignment_id, status_id, created_at, updated_at) VALUES
(1, 1, 8, NOW(), NOW()),
(1, 2, 8, NOW(), NOW()),
(1, 3, 8, NOW(), NOW()),
(2, 1, 8, NOW(), NOW()),
(2, 2, 8, NOW(), NOW()),
(2, 4, 8, NOW(), NOW()),
(3, 5, 8, NOW(), NOW()),
(3, 6, 8, NOW(), NOW());

INSERT IGNORE INTO course_grades (student_id, assignment_id, usual_score, midterm_score, final_score, total_score, gpa_grade, status_id, created_at, updated_at) VALUES
(1, 1, 85.5, 88.0, 92.0, 88.5, 3.7, 1, NOW(), NOW()),
(1, 2, 78.0, 82.5, 85.0, 81.8, 3.0, 1, NOW(), NOW()),
(1, 3, 90.0, 87.5, 89.0, 88.8, 3.7, 1, NOW(), NOW()),
(2, 1, 92.0, 89.5, 91.0, 90.8, 3.7, 1, NOW(), NOW()),
(2, 2, 88.5, 85.0, 87.5, 87.0, 3.3, 1, NOW(), NOW()),
(2, 4, 76.0, 79.5, 82.0, 79.2, 2.7, 1, NOW(), NOW()),
(3, 5, 83.5, 86.0, 88.5, 86.0, 3.3, 1, NOW(), NOW()),
(3, 6, 79.0, 81.5, 84.0, 81.5, 3.0, 1, NOW(), NOW());

SET FOREIGN_KEY_CHECKS = 1;