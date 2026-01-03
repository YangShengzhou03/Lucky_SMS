DROP DATABASE IF EXISTS lucky_sms;
CREATE DATABASE lucky_sms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lucky_sms;
SET NAMES utf8mb4;

CREATE TABLE status_types (
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(50) NOT NULL,
    status_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_status_code (status_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE course_types (
    course_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE selection_types (
    selection_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE titles (
    title_id INT AUTO_INCREMENT PRIMARY KEY,
    title_name VARCHAR(50) NOT NULL,
    title_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_title_code (title_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE gender_types (
    gender_id INT AUTO_INCREMENT PRIMARY KEY,
    gender_name VARCHAR(10) NOT NULL,
    gender_code VARCHAR(10) NOT NULL UNIQUE,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_gender_code (gender_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_types (
    user_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE semester_types (
    semester_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE education_levels (
    education_level_id INT AUTO_INCREMENT PRIMARY KEY,
    level_name VARCHAR(50) NOT NULL,
    level_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_level_code (level_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE announcement_types (
    announcement_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    type_code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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

CREATE TABLE teachers (
    teacher_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    teacher_no VARCHAR(20) NOT NULL UNIQUE,
    department_id INT,
    title_id INT,
    status_id INT DEFAULT 1,
    hire_date DATE,
    office_location VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    research_direction VARCHAR(100),
    education_background VARCHAR(50),
    work_experience TEXT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE SET NULL,
    FOREIGN KEY (title_id) REFERENCES titles(title_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES status_types(status_id) ON DELETE SET NULL,
    INDEX idx_teacher_no (teacher_no),
    INDEX idx_department (department_id),
    INDEX idx_research_direction (research_direction)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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

CREATE TABLE announcements (
    announcement_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    publisher_id INT NOT NULL,
    department_id INT,
    announcement_type_id INT NOT NULL DEFAULT 1,
    priority INT DEFAULT 0,
    target_audience VARCHAR(20) DEFAULT 'ALL',
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

CREATE TABLE todos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    text VARCHAR(255) NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    due_date DATE,
    important BOOLEAN DEFAULT FALSE,
    category VARCHAR(50),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_completed (completed)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO status_types (status_name, status_code, description) VALUES
('正常', 'active', '正常状态'),
('禁用', 'disabled', '禁用状态'),
('已删除', 'deleted', '已删除状态'),
('待审核', 'pending', '待审核状态'),
('已通过', 'approved', '已通过状态'),
('已拒绝', 'rejected', '已拒绝状态');

INSERT INTO course_types (type_name, type_code, description) VALUES
('必修课', 'compulsory', '必修课程'),
('选修课', 'elective', '选修课程'),
('通识课', 'general', '通识课程'),
('实践课', 'practice', '实践课程');

INSERT INTO selection_types (type_name, type_code, description) VALUES
('正常选课', 'normal', '正常选课'),
('补选', 'makeup', '补选课程'),
('重修', 'retake', '重修课程'),
('免修', 'exempt', '免修课程');

INSERT INTO titles (title_name, title_code, description) VALUES
('助教', 'assistant', '助教'),
('讲师', 'lecturer', '讲师'),
('副教授', 'associate_professor', '副教授'),
('教授', 'professor', '教授');

INSERT INTO gender_types (gender_name, gender_code) VALUES
('男', 'MALE'),
('女', 'FEMALE'),
('其他', 'OTHER');

INSERT INTO user_types (type_name, type_code, description) VALUES
('学生', 'STUDENT', '在校学生'),
('教师', 'TEACHER', '教师用户'),
('管理员', 'ADMIN', '系统管理员');

INSERT INTO semester_types (type_name, type_code, description) VALUES
('第一学期', 'FIRST', '秋季学期'),
('第二学期', 'SECOND', '春季学期');

INSERT INTO education_levels (level_name, level_code, description) VALUES
('本科', 'UNDERGRADUATE', '本科教育'),
('硕士', 'MASTER', '硕士研究生'),
('博士', 'DOCTOR', '博士研究生');

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

INSERT INTO departments (department_name, department_code, description) VALUES
('计算机学院', 'CS', '计算机科学与技术学院'),
('软件学院', 'SE', '软件学院');

INSERT INTO majors (major_name, major_code, department_id, description) VALUES
('计算机科学与技术', 'CS001', 1, '计算机科学与技术专业'),
('软件工程', 'SE001', 2, '软件工程专业');

INSERT INTO classes (class_name, class_code, major_id, department_id, grade_year) VALUES
('计算机1班', 'CS2024001', 1, 1, 2024),
('软件工程1班', 'SE2024001', 2, 2, 2024);

INSERT INTO semesters (semester_name, semester_code, semester_type_id, start_date, end_date) VALUES
('2024-2025第一学期', '2024-2025-1', 1, '2024-09-01', '2025-01-20'),
('2024-2025第二学期', '2024-2025-2', 2, '2025-02-20', '2025-07-10');

INSERT INTO courses (course_code, course_name, course_type_id, credit, total_hours, theory_hours, practice_hours, department_id, is_compulsory) VALUES
('CS101', '程序设计基础', 1, 4.0, 64, 48, 16, 1, TRUE),
('CS102', '数据结构', 1, 4.0, 64, 48, 16, 1, TRUE),
('SE101', '软件工程导论', 1, 3.0, 48, 32, 16, 2, TRUE),
('SE102', 'Web开发技术', 2, 3.0, 48, 24, 24, 2, FALSE);

INSERT INTO users (username, password, phone, email, real_name, gender_id, user_type_id, status_id) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138000', 'admin@lucky-sms.com', '管理员', 1, 3, 1),
('teacher1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138001', 'teacher1@lucky-sms.com', '张老师', 1, 2, 1),
('student1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '13800138002', 'student1@lucky-sms.com', '李同学', 1, 1, 1);

INSERT INTO students (user_id, student_no, department_id, major_id, class_id, enrollment_date, education_years, education_level_id, status_id) VALUES
(3, '2024001', 1, 1, 1, '2024-09-01', 4, 1, 1);

INSERT INTO teachers (user_id, teacher_no, department_id, title_id, status_id, education, research_area) VALUES
(2, 'T2024001', 1, 2, 1, '博士', '软件工程');

INSERT INTO teaching_assignments (course_id, teacher_id, semester_id, class_id, max_students, classroom, schedule, status_id) VALUES
(1, 1, 1, 1, 50, 'A101', '周一 1-2节', 1),
(2, 1, 1, 1, 50, 'A102', '周二 3-4节', 1),
(3, 1, 1, 2, 50, 'B101', '周三 1-2节', 1),
(4, 1, 1, 2, 50, 'B102', '周四 3-4节', 1);

INSERT INTO course_selections (student_id, assignment_id, selection_type_id, status_id) VALUES
(1, 1, 1, 1),
(1, 2, 1, 1);

INSERT INTO course_grades (selection_id, usual_score, midterm_score, final_score, total_score, grade_level) VALUES
(1, 85.0, 88.0, 90.0, 88.0, 'A'),
(2, 82.0, 85.0, 87.0, 85.0, 'A-');

INSERT INTO announcements (title, content, publisher_id, announcement_type_id, priority, target_audience, status_id) VALUES
('系统上线通知', '欢迎使用Lucky SMS学生管理系统！', 1, 1, 1, 'ALL', 1),
('选课通知', '2024-2025第一学期选课开始时间为2024年9月1日', 1, 6, 0, 'STUDENT', 1);

INSERT INTO announcement_reads (announcement_id, user_id) VALUES
(1, 2),
(1, 3),
(2, 3);

INSERT INTO todos (user_id, title, description, completed, due_date) VALUES
(3, '完成作业', '完成程序设计基础作业', FALSE, '2025-01-10'),
(3, '准备考试', '复习数据结构', FALSE, '2025-01-15'),
(3, '提交实验报告', '提交Web开发技术实验报告', TRUE, '2025-01-05');
