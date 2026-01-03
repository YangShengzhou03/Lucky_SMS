-- ========================================
-- Lucky_SMS 数据库初始化脚本
-- 版本: 2.0
-- 创建日期: 2026-01-03
-- 功能: 完整的教务管理系统数据库，支持所有CRUD操作
-- ========================================

DROP DATABASE IF EXISTS lucky_sms;
CREATE DATABASE lucky_sms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lucky_sms;
SET NAMES utf8mb4;

-- ========================================
-- 基础字典表
-- ========================================

-- 状态类型表
CREATE TABLE status_types (
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL,
    status_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_status_code (status_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 课程类型表
CREATE TABLE course_types (
    course_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 选课类型表
CREATE TABLE selection_types (
    selection_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 职称表
CREATE TABLE titles (
    title_id INT AUTO_INCREMENT PRIMARY KEY,
    title_name VARCHAR(50) NOT NULL,
    title_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_title_code (title_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 性别类型表
CREATE TABLE gender_types (
    gender_id INT AUTO_INCREMENT PRIMARY KEY,
    gender_name VARCHAR(10) NOT NULL,
    gender_code VARCHAR(10) NOT NULL UNIQUE,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_gender_code (gender_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户类型表
CREATE TABLE user_types (
    user_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 学期类型表
CREATE TABLE semester_types (
    semester_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 学历等级表
CREATE TABLE education_levels (
    education_level_id INT AUTO_INCREMENT PRIMARY KEY,
    level_name VARCHAR(50) NOT NULL,
    level_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_level_code (level_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 公告类型表
CREATE TABLE announcement_types (
    announcement_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- 核心业务表
-- ========================================

-- 用户表
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    avatar VARCHAR(255),
    real_name VARCHAR(50),
    gender_id INT DEFAULT 1,
    birthday DATE,
    address VARCHAR(255),
    user_type_id INT NOT NULL DEFAULT 1,
    status_id INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (gender_id) REFERENCES gender_types(gender_id) ON DELETE SET NULL,
    FOREIGN KEY (user_type_id) REFERENCES user_types(user_type_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_email (email),
    INDEX idx_real_name (real_name),
    INDEX idx_user_type (user_type_id),
    INDEX idx_status (status_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 学生表
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    student_no VARCHAR(20) NOT NULL UNIQUE,
    department_id INT,
    major_id INT,
    class_id INT,
    enrollment_date DATE,
    graduation_date DATE,
    education_years INT DEFAULT 4,
    education_level_id INT DEFAULT 1,
    status_id INT DEFAULT 1,
    emergency_contact VARCHAR(50),
    emergency_phone VARCHAR(20),
    parent_name VARCHAR(50),
    parent_phone VARCHAR(20),
    home_address VARCHAR(255),
    high_school VARCHAR(100),
    entrance_score DECIMAL(5,2),
    scholarship_info TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (major_id) REFERENCES majors(major_id) ON DELETE SET NULL,
    FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE SET NULL,
    FOREIGN KEY (education_level_id) REFERENCES education_levels(education_level_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_student_no (student_no),
    INDEX idx_department (department_id),
    INDEX idx_enrollment_date (enrollment_date),
    INDEX idx_graduation_date (graduation_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 教师表
CREATE TABLE teachers (
    teacher_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    teacher_no VARCHAR(20) NOT NULL UNIQUE,
    department_id INT,
    title_id INT,
    status_id INT DEFAULT 1,
    education VARCHAR(50),
    research_area VARCHAR(100),
    office_phone VARCHAR(20),
    office_location VARCHAR(100),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (title_id) REFERENCES titles(title_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_teacher_no (teacher_no),
    INDEX idx_department (department_id),
    INDEX idx_research_area (research_area)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 部门表
CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL,
    department_code VARCHAR(20) NOT NULL UNIQUE,
    description TEXT,
    status_id INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_department_code (department_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 专业表
CREATE TABLE majors (
    major_id INT AUTO_INCREMENT PRIMARY KEY,
    major_name VARCHAR(100) NOT NULL,
    major_code VARCHAR(20) NOT NULL UNIQUE,
    department_id INT,
    description TEXT,
    status_id INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_major_code (major_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 班级表
CREATE TABLE classes (
    class_id INT AUTO_INCREMENT PRIMARY KEY,
    class_name VARCHAR(50) NOT NULL,
    class_code VARCHAR(20) NOT NULL UNIQUE,
    major_id INT,
    department_id INT,
    grade_year INT,
    student_count INT DEFAULT 0,
    status_id INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (major_id) REFERENCES majors(major_id) ON DELETE SET NULL,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_class_code (class_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 课程表
CREATE TABLE courses (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    course_type_id INT DEFAULT 1,
    credit DECIMAL(3,1) NOT NULL,
    total_hours INT NOT NULL,
    theory_hours INT DEFAULT 0,
    practice_hours INT DEFAULT 0,
    department_id INT,
    major_id INT,
    description TEXT,
    syllabus TEXT,
    textbook VARCHAR(255),
    prerequisites TEXT,
    is_compulsory BOOLEAN DEFAULT TRUE,
    status_id INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_type_id) REFERENCES course_types(course_type_id) ON DELETE SET NULL,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (major_id) REFERENCES majors(major_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_course_code (course_code),
    INDEX idx_course_name (course_name),
    INDEX idx_department (department_id),
    INDEX idx_is_compulsory (is_compulsory)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 学期表
CREATE TABLE semesters (
    semester_id INT AUTO_INCREMENT PRIMARY KEY,
    semester_name VARCHAR(50) NOT NULL,
    semester_code VARCHAR(20) NOT NULL UNIQUE,
    semester_type_id INT DEFAULT 1,
    start_date DATE,
    end_date DATE,
    status_id INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (semester_type_id) REFERENCES semester_types(semester_type_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_semester_code (semester_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 教学安排表
CREATE TABLE teaching_assignments (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    teacher_id INT NOT NULL,
    semester_id INT NOT NULL,
    class_id INT,
    max_students INT DEFAULT 50,
    current_students INT DEFAULT 0,
    classroom VARCHAR(50),
    schedule VARCHAR(255),
    status_id INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE,
    FOREIGN KEY (semester_id) REFERENCES semesters(semester_id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES classes(class_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_course_semester (course_id, semester_id),
    INDEX idx_teacher (teacher_id),
    INDEX idx_semester (semester_id),
    INDEX idx_class (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 选课记录表
CREATE TABLE course_selections (
    selection_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    assignment_id INT NOT NULL,
    selection_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    selection_type_id INT DEFAULT 1,
    priority INT DEFAULT 1,
    status_id INT DEFAULT 1,
    drop_time DATETIME,
    drop_reason VARCHAR(255),
    final_grade DECIMAL(5,2),
    attendance_rate DECIMAL(5,2),
    evaluation_score DECIMAL(5,2),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (assignment_id) REFERENCES teaching_assignments(assignment_id) ON DELETE CASCADE,
    FOREIGN KEY (selection_type_id) REFERENCES selection_types(selection_type_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    UNIQUE KEY uk_student_assignment (student_id, assignment_id),
    INDEX idx_student_semester (student_id, assignment_id),
    INDEX idx_selection_time (selection_time),
    INDEX idx_drop_time (drop_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 成绩表
CREATE TABLE course_grades (
    grade_id INT AUTO_INCREMENT PRIMARY KEY,
    selection_id INT NOT NULL,
    usual_score DECIMAL(5,2),
    midterm_score DECIMAL(5,2),
    final_score DECIMAL(5,2),
    total_score DECIMAL(5,2),
    grade_level VARCHAR(10),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (selection_id) REFERENCES course_selections(selection_id) ON DELETE CASCADE,
    INDEX idx_selection (selection_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 公告表
CREATE TABLE announcements (
    announcement_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    publisher_id INT NOT NULL,
    department_id INT,
    announcement_type_id INT NOT NULL DEFAULT 1,
    priority INT DEFAULT 0 COMMENT '优先级：0-普通，1-重要，2-紧急',
    target_audience VARCHAR(20) DEFAULT 'ALL' COMMENT '受众：ALL-全体，STUDENT-学生，TEACHER-教师',
    status_id INT DEFAULT 1,
    publish_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (publisher_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (announcement_type_id) REFERENCES announcement_types(announcement_type_id),
    FOREIGN KEY (status_id) REFERENCES status_types(status_id),
    INDEX idx_publish_time (publish_time),
    INDEX idx_department (department_id),
    INDEX idx_type (announcement_type_id),
    INDEX idx_audience (target_audience),
    INDEX idx_publisher (publisher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 公告阅读记录表
CREATE TABLE announcement_reads (
    read_id INT AUTO_INCREMENT PRIMARY KEY,
    announcement_id INT NOT NULL,
    user_id INT NOT NULL,
    read_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (announcement_id) REFERENCES announcements(announcement_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_announcement_user (announcement_id, user_id),
    INDEX idx_user (user_id),
    INDEX idx_announcement (announcement_id),
    INDEX idx_read_time (read_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- 插入字典数据
-- ========================================

-- 状态类型
INSERT INTO status_types (status_name, status_code, description) VALUES
('正常', 'active', '正常状态'),
('禁用', 'disabled', '禁用状态'),
('已删除', 'deleted', '已删除状态'),
('待审核', 'pending', '待审核状态'),
('已通过', 'approved', '已通过状态'),
('已拒绝', 'rejected', '已拒绝状态');

-- 课程类型
INSERT INTO course_types (type_name, type_code, description) VALUES
('必修课', 'compulsory', '必修课程'),
('选修课', 'elective', '选修课程'),
('通识课', 'general', '通识课程'),
('实践课', 'practice', '实践课程');

-- 选课类型
INSERT INTO selection_types (type_name, type_code, description) VALUES
('正常选课', 'normal', '正常选课'),
('补选', 'makeup', '补选课程'),
('重修', 'retake', '重修课程'),
('免修', 'exempt', '免修课程');

-- 职称
INSERT INTO titles (title_name, title_code, description) VALUES
('助教', 'assistant', '助教'),
('讲师', 'lecturer', '讲师'),
('副教授', 'associate_professor', '副教授'),
('教授', 'professor', '教授');

-- 性别类型
INSERT INTO gender_types (gender_name, gender_code) VALUES
('男', 'MALE'),
('女', 'FEMALE'),
('其他', 'OTHER');

-- 用户类型
INSERT INTO user_types (type_name, type_code, description) VALUES
('学生', 'STUDENT', '在校学生'),
('教师', 'TEACHER', '教师用户'),
('管理员', 'ADMIN', '系统管理员');

-- 学期类型
INSERT INTO semester_types (type_name, type_code, description) VALUES
('第一学期', 'FIRST', '秋季学期'),
('第二学期', 'SECOND', '春季学期');

-- 学历等级
INSERT INTO education_levels (level_name, level_code, description) VALUES
('本科', 'UNDERGRADUATE', '本科教育'),
('硕士', 'MASTER', '硕士研究生'),
('博士', 'DOCTOR', '博士研究生');

-- 公告类型
INSERT INTO announcement_types (type_name, type_code, description) VALUES
('通知公告', 'NOTICE', '一般性通知公告'),
('校园招聘', 'RECRUITMENT', '校园招聘信息'),
('学术讲座', 'LECTURE', '学术讲座信息'),
('活动通知', 'EVENT', '校园活动通知'),
('紧急通知', 'URGENT', '紧急重要通知'),
('教学安排', 'TEACHING', '教学安排通知'),
('成果申报', 'ACHIEVEMENT', '教学成果申报通知'),
('培训通知', 'TRAINING', '教师培训通知'),
('检查通知', 'INSPECTION', '教学检查通知');

-- ========================================
-- 插入基础数据
-- ========================================

-- 部门
INSERT INTO departments (department_name, department_code, description) VALUES
('计算机学院', 'CS', '计算机科学与技术学院'),
('软件学院', 'SE', '软件学院'),
('信息学院', 'IS', '信息学院'),
('数学学院', 'MA', '数学学院'),
('外语学院', 'FL', '外语学院'),
('教务处', 'AA', '教务处'),
('科研处', 'RA', '科研处'),
('教师发展中心', 'TDC', '教师发展中心'),
('教学质量监控中心', 'QMC', '教学质量监控中心'),
('就业指导中心', 'EC', '就业指导中心');

-- 专业
INSERT INTO majors (major_name, major_code, department_id, description) VALUES
('计算机科学与技术', 'CS001', 1, '计算机科学与技术专业'),
('软件工程', 'SE001', 2, '软件工程专业'),
('信息管理与信息系统', 'IS001', 3, '信息管理与信息系统专业'),
('数学与应用数学', 'MA001', 4, '数学与应用数学专业'),
('英语', 'EN001', 5, '英语专业');

-- 班级
INSERT INTO classes (class_name, class_code, major_id, department_id, grade_year) VALUES
('计算机1班', 'CS2024001', 1, 1, 2024),
('计算机2班', 'CS2024002', 1, 1, 2024),
('软件工程1班', 'SE2024001', 2, 2, 2024),
('软件工程2班', 'SE2024002', 2, 2, 2024);

-- 学期
INSERT INTO semesters (semester_name, semester_code, semester_type_id, start_date, end_date) VALUES
('2024-2025第一学期', '2024-2025-1', 1, '2024-09-01', '2025-01-20'),
('2024-2025第二学期', '2024-2025-2', 2, '2025-02-20', '2025-07-10'),
('2025-2026第一学期', '2025-2026-1', 1, '2025-09-01', '2026-01-20');

-- 课程
INSERT INTO courses (course_code, course_name, course_type_id, credit, total_hours, theory_hours, practice_hours, department_id, is_compulsory) VALUES
('CS101', '程序设计基础', 1, 4.0, 64, 48, 16, 1, TRUE),
('CS102', '数据结构', 1, 4.0, 64, 48, 16, 1, TRUE),
('CS103', '算法分析', 1, 3.0, 48, 40, 8, 1, TRUE),
('CS104', '数据库原理', 1, 3.0, 48, 32, 16, 1, TRUE),
('CS105', '操作系统', 1, 3.0, 48, 40, 8, 1, TRUE),
('SE101', '软件工程导论', 1, 3.0, 48, 32, 16, 2, TRUE),
('SE102', 'Web开发技术', 2, 3.0, 48, 24, 24, 2, FALSE),
('SE103', '移动应用开发', 2, 3.0, 48, 24, 24, 2, FALSE);

-- 用户
INSERT INTO users (username, password, real_name, user_type_id, gender_id) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', 3, 1),
('teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张教授', 2, 1),
('teacher2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '李讲师', 2, 2),
('student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '王同学', 1, 1),
('student2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '李同学', 1, 2);

-- 教师
INSERT INTO teachers (user_id, teacher_no, department_id, title_id) VALUES
(2, 'T2024001', 1, 4),
(3, 'T2024002', 2, 2);

-- 学生
INSERT INTO students (user_id, student_no, department_id, major_id, class_id, enrollment_date) VALUES
(4, 'S2024001', 1, 1, 1, '2024-09-01'),
(5, 'S2024002', 2, 2, 3, '2024-09-01');

-- 教学安排
INSERT INTO teaching_assignments (course_id, teacher_id, semester_id, class_id, max_students, classroom, schedule) VALUES
(1, 1, 1, 1, 50, 'A101', '周一 1-2节'),
(2, 1, 1, 1, 50, 'A102', '周二 3-4节'),
(3, 1, 1, 1, 50, 'A103', '周三 1-2节'),
(4, 2, 1, 3, 50, 'B101', '周四 3-4节'),
(6, 2, 1, 3, 50, 'B102', '周五 1-2节'),
(7, 2, 1, 3, 50, 'B103', '周一 3-4节');

-- 选课记录
INSERT INTO course_selections (student_id, assignment_id, selection_type_id) VALUES
(1, 1, 1),
(1, 2, 1),
(1, 3, 1),
(2, 4, 1),
(2, 6, 1);

-- 成绩
INSERT INTO course_grades (selection_id, usual_score, midterm_score, final_score, total_score, grade_level) VALUES
(1, 85.0, 88.0, 90.0, 88.0, 'A'),
(2, 82.0, 85.0, 87.0, 85.0, 'A'),
(3, 78.0, 80.0, 82.0, 80.0, 'B'),
(4, 75.0, 78.0, 80.0, 78.0, 'B'),
(5, 88.0, 90.0, 92.0, 90.0, 'A');

-- 公告
INSERT INTO announcements (title, content, publisher_id, department_id, announcement_type_id, priority, target_audience, publish_time) VALUES
('关于2023-2024学年第二学期教学安排的通知', '根据学校教学工作安排，现将2023-2024学年第二学期教学安排通知如下...', 1, 6, 6, 1, 'ALL', '2023-11-01 00:00:00'),
('2023年度教学成果奖申报通知', '为表彰在教学工作中取得突出成绩的教师，学校决定开展2023年度教学成果奖申报工作...', 1, 7, 7, 1, 'TEACHER', '2023-10-25 00:00:00'),
('关于举办教师信息化教学能力提升培训的通知', '为进一步提升教师信息化教学能力，学校决定举办教师信息化教学能力提升培训...', 1, 8, 8, 0, 'TEACHER', '2023-10-20 00:00:00'),
('关于开展期中教学检查工作的通知', '为全面了解本学期教学运行情况，提高教学质量，学校决定开展期中教学检查工作...', 1, 9, 9, 1, 'TEACHER', '2023-10-15 00:00:00'),
('校园招聘信息：腾讯2024校园招聘', '腾讯公司2024校园招聘正式启动，欢迎各位同学踊跃报名...', 1, 10, 2, 1, 'STUDENT', '2023-12-11 00:00:00');

-- ========================================
-- 创建视图（简化查询）
-- ========================================

-- 学生信息视图
CREATE OR REPLACE VIEW v_student_info AS
SELECT
    s.student_id,
    s.student_no,
    u.user_id,
    u.username,
    u.real_name,
    u.phone,
    u.email,
    u.avatar,
    u.gender_id,
    gt.gender_name AS gender,
    u.birthday,
    s.department_id,
    d.department_name,
    s.major_id,
    m.major_name,
    s.class_id,
    c.class_name,
    s.enrollment_date,
    s.graduation_date,
    s.education_years,
    el.level_name AS education_level,
    s.status_id,
    st.status_name AS status
FROM students s
LEFT JOIN users u ON s.user_id = u.user_id
LEFT JOIN gender_types gt ON u.gender_id = gt.gender_id
LEFT JOIN departments d ON s.department_id = d.department_id
LEFT JOIN majors m ON s.major_id = m.major_id
LEFT JOIN classes c ON s.class_id = c.class_id
LEFT JOIN education_levels el ON s.education_level_id = el.education_level_id
LEFT JOIN status_types st ON s.status_id = st.status_id;

-- 教师信息视图
CREATE OR REPLACE VIEW v_teacher_info AS
SELECT
    t.teacher_id,
    t.teacher_no,
    u.user_id,
    u.username,
    u.real_name,
    u.phone,
    u.email,
    u.avatar,
    u.gender_id,
    gt.gender_name AS gender,
    u.birthday,
    t.department_id,
    d.department_name,
    t.title_id,
    ti.title_name,
    t.education,
    t.research_area,
    t.office_phone,
    t.office_location,
    t.status_id,
    st.status_name AS status
FROM teachers t
LEFT JOIN users u ON t.user_id = u.user_id
LEFT JOIN gender_types gt ON u.gender_id = gt.gender_id
LEFT JOIN departments d ON t.department_id = d.department_id
LEFT JOIN titles ti ON t.title_id = ti.title_id
LEFT JOIN status_types st ON t.status_id = st.status_id;

-- 课程信息视图
CREATE OR REPLACE VIEW v_course_info AS
SELECT
    c.course_id,
    c.course_code,
    c.course_name,
    c.course_type_id,
    ct.type_name AS course_type,
    c.credit,
    c.total_hours,
    c.theory_hours,
    c.practice_hours,
    c.department_id,
    d.department_name,
    c.major_id,
    m.major_name,
    c.description,
    c.syllabus,
    c.textbook,
    c.prerequisites,
    c.is_compulsory,
    c.status_id,
    st.status_name AS status
FROM courses c
LEFT JOIN course_types ct ON c.course_type_id = ct.course_type_id
LEFT JOIN departments d ON c.department_id = d.department_id
LEFT JOIN majors m ON c.major_id = m.major_id
LEFT JOIN status_types st ON c.status_id = st.status_id;

-- 公告信息视图
CREATE OR REPLACE VIEW v_announcement_info AS
SELECT
    a.announcement_id,
    a.title,
    a.content,
    a.publisher_id,
    u.real_name AS publisher_name,
    u.username AS publisher_username,
    a.department_id,
    d.department_name,
    a.announcement_type_id,
    at.type_name AS announcement_type,
    a.priority,
    a.target_audience,
    a.status_id,
    st.status_name AS status,
    a.publish_time,
    a.create_time,
    a.update_time,
    (SELECT COUNT(*) FROM announcement_reads ar WHERE ar.announcement_id = a.announcement_id) AS read_count
FROM announcements a
LEFT JOIN users u ON a.publisher_id = u.user_id
LEFT JOIN departments d ON a.department_id = d.department_id
LEFT JOIN announcement_types at ON a.announcement_type_id = at.announcement_type_id
LEFT JOIN status_types st ON a.status_id = st.status_id;

-- ========================================
-- 完成初始化
-- ========================================
SELECT '数据库初始化完成！' AS message;
SELECT COUNT(*) AS total_tables FROM information_schema.tables WHERE table_schema = 'lucky_sms';
