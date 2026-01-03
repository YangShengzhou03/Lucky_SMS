# Lucky SMS - Go Backend API Documentation

## 项目简介

Lucky SMS 是一个学生管理系统后端服务，使用 Go 语言开发，基于 Gin 框架和 GORM ORM，提供完整的 RESTful API 接口。

## 技术栈

- **框架**: Gin Web Framework
- **ORM**: GORM
- **数据库**: MySQL
- **认证**: JWT (golang-jwt/jwt/v5)
- **配置**: Viper
- **密码加密**: bcrypt (golang.org/x/crypto)

## 项目结构

```
backend-go/
├── config/           # 配置管理
├── docs/            # API文档
├── handler/          # 控制器层
├── middleware/       # 中间件
├── models/           # 数据模型
├── repository/       # 数据访问层
├── router/           # 路由配置
├── service/          # 业务逻辑层
├── utils/           # 工具函数
├── vo/              # 视图对象
├── main.go          # 主程序入口
├── go.mod           # Go模块定义
└── config.yaml      # 配置文件
```

## 快速开始

### 环境要求

- Go 1.21+
- MySQL 5.7+
- Git

### 安装依赖

```bash
go mod download
```

### 配置数据库

编辑 `config.yaml` 文件，修改数据库连接信息：

```yaml
database:
  host: localhost
  port: 3306
  username: root
  password: your_password
  dbname: lucky_sms
```

### 运行项目

```bash
go run main.go
```

服务将在 `http://localhost:8081` 启动。

## API 接口文档

### 基础信息

- **Base URL**: `http://localhost:8081/api`
- **Content-Type**: `application/json`

### 认证方式

除登录接口外，所有接口都需要在请求头中携带 JWT Token：

```
Authorization: Bearer <token>
```

### 通用响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 用户模块

### 1. 手机号登录

**接口**: `POST /api/login/phone`

**请求参数**:
```json
{
  "phone": "13800138000",
  "captcha": "123456"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "uid": 1,
    "phone": "138****8000",
    "role": "STUDENT",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### 2. 密码登录

**接口**: `POST /api/login/password`

**请求参数**:
```json
{
  "phone": "13800138000",
  "password": "123456"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "uid": 1,
    "phone": "138****8000",
    "role": "STUDENT",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### 3. 重置密码

**接口**: `POST /api/reset-password`

**请求参数**:
```json
{
  "phone": "13800138000",
  "password": "newpassword"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "重置密码成功",
  "data": {
    "success": true
  }
}
```

## 学生模块

### 1. 获取首页信息

**接口**: `GET /api/student/home`

**需要认证**: 是

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "studentName": "张三",
    "studentNo": "20240001",
    "className": "计算机1班",
    "majorName": "计算机科学与技术",
    "departmentName": "信息工程学院"
  }
}
```

### 2. 获取成绩列表

**接口**: `GET /api/student/grades`

**需要认证**: 是

**查询参数**:
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "gradeId": 1,
        "courseId": 1,
        "courseCode": "CS101",
        "courseName": "计算机基础",
        "credit": 3.0,
        "semesterId": 1,
        "semesterName": "2024-2025-第一学期",
        "regularScore": 85.0,
        "finalScore": 90.0,
        "totalScore": 88.0,
        "gradePoint": 4.0,
        "gradeLevel": "A"
      }
    ],
    "total": 10,
    "page": 1,
    "pageSize": 10
  }
}
```

### 3. 获取可选课程

**接口**: `GET /api/student/courses/available`

**需要认证**: 是

**查询参数**:
- `semesterId`: 学期ID（默认1）
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "获取课程列表成功",
  "data": {
    "list": [
      {
        "selectionId": 1,
        "courseId": 1,
        "courseCode": "CS101",
        "courseName": "计算机基础",
        "credit": 3.0,
        "teacherName": "李老师",
        "totalHours": 48,
        "selectedCount": 30,
        "maxCapacity": 50,
        "isSelected": false
      }
    ],
    "total": 20,
    "page": 1,
    "pageSize": 10
  }
}
```

### 4. 选课

**接口**: `POST /api/student/courses/select`

**需要认证**: 是

**请求参数**:
```json
{
  "courseId": 1,
  "semesterId": 1
}
```

**响应**:
```json
{
  "code": 200,
  "message": "选课成功",
  "data": {
    "success": true,
    "message": "选课成功"
  }
}
```

### 5. 退课

**接口**: `POST /api/student/courses/drop`

**需要认证**: 是

**请求参数**:
```json
{
  "courseId": 1
}
```

**响应**:
```json
{
  "code": 200,
  "message": "退课成功",
  "data": {
    "success": true,
    "message": "退课成功"
  }
}
```

### 6. 获取选课统计

**接口**: `GET /api/student/courses/statistics`

**需要认证**: 是

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "totalCredits": 18.0,
    "selectedCourses": 6,
    "compulsoryCourses": 4,
    "electiveCourses": 2
  }
}
```

### 7. 获取个人信息

**接口**: `GET /api/student/profile`

**需要认证**: 是

**响应**:
```json
{
  "code": 200,
  "message": "获取个人信息成功",
  "data": {
    "studentId": 1,
    "studentNo": "20240001",
    "userId": 1,
    "realName": "张三",
    "genderId": 1,
    "birthday": "2000-01-01",
    "phone": "138****8000",
    "email": "student@example.com",
    "address": "北京市",
    "departmentId": 1,
    "majorId": 1,
    "classId": 1,
    "enrollmentDate": "2024-09-01",
    "graduationDate": "2028-06-30",
    "educationYears": 4,
    "educationLevelId": 1,
    "statusId": 1,
    "emergencyContact": "张父",
    "emergencyPhone": "13900139000",
    "parentName": "张父",
    "parentPhone": "13900139000",
    "homeAddress": "北京市海淀区",
    "highSchool": "北京一中",
    "entranceScore": "650.00",
    "scholarshipInfo": "无"
  }
}
```

### 8. 更新个人信息

**接口**: `POST /api/student/profile`

**需要认证**: 是

**请求参数**:
```json
{
  "realName": "张三",
  "genderId": 1,
  "birthday": "2000-01-01",
  "phone": "13800138000",
  "email": "student@example.com",
  "address": "北京市",
  "emergencyContact": "张父",
  "emergencyPhone": "13900139000",
  "parentName": "张父",
  "parentPhone": "13900139000",
  "homeAddress": "北京市海淀区"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "更新个人信息成功",
  "data": null
}
```

### 9. 获取学生状态

**接口**: `GET /api/student/status`

**需要认证**: 是

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "statusId": 1,
    "statusName": "在读"
  }
}
```

## 教师模块

### 1. 获取首页信息

**接口**: `GET /api/teacher/home`

**需要认证**: 是

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "teacherName": "李老师",
    "teacherNo": "T2024001",
    "titleName": "副教授",
    "departmentName": "信息工程学院"
  }
}
```

### 2. 获取个人信息

**接口**: `GET /api/teacher/profile`

**需要认证**: 是

**响应**:
```json
{
  "code": 200,
  "message": "获取个人信息成功",
  "data": {
    "teacherId": 1,
    "teacherNo": "T2024001",
    "userId": 2,
    "realName": "李老师",
    "genderId": 1,
    "birthday": "1980-01-01",
    "phone": "13900139000",
    "email": "teacher@example.com",
    "address": "北京市",
    "departmentId": 1,
    "titleId": 2,
    "hireDate": "2010-09-01",
    "officeLocation": "信息楼301",
    "researchDirection": "人工智能",
    "educationBackground": "博士",
    "workExperience": "10年教学经验"
  }
}
```

### 3. 获取教师课程

**接口**: `GET /api/teacher/courses`

**需要认证**: 是

**查询参数**:
- `semesterId`: 学期ID（默认1）
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "courseId": 1,
        "courseCode": "CS101",
        "courseName": "计算机基础",
        "credit": 3.0,
        "totalHours": 48,
        "theoryHours": 32,
        "practiceHours": 16,
        "selectedCount": 30,
        "semesterId": 1,
        "semesterName": "2024-2025-第一学期"
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 10
  }
}
```

### 4. 获取课程学生

**接口**: `GET /api/teacher/courses/:courseId/students`

**需要认证**: 是

**路径参数**:
- `courseId`: 课程ID

**查询参数**:
- `semesterId`: 学期ID（默认1）
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "studentId": 1,
        "studentNo": "20240001",
        "realName": "张三",
        "classId": 1,
        "className": "计算机1班",
        "majorId": 1,
        "majorName": "计算机科学与技术",
        "departmentId": 1,
        "departmentName": "信息工程学院"
      }
    ],
    "total": 30,
    "page": 1,
    "pageSize": 10
  }
}
```

### 5. 获取课程成绩

**接口**: `GET /api/teacher/courses/:courseId/grades`

**需要认证**: 是

**路径参数**:
- `courseId`: 课程ID

**查询参数**:
- `semesterId`: 学期ID（默认1）
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "gradeId": 1,
        "studentId": 1,
        "studentNo": "20240001",
        "studentName": "张三",
        "courseId": 1,
        "courseCode": "CS101",
        "courseName": "计算机基础",
        "semesterId": 1,
        "semesterName": "2024-2025-第一学期",
        "regularScore": 85.0,
        "finalScore": 90.0,
        "totalScore": 88.0,
        "gradePoint": 4.0,
        "gradeLevel": "A",
        "gradedAt": "2024-12-20 10:00:00"
      }
    ],
    "total": 30,
    "page": 1,
    "pageSize": 10
  }
}
```

### 6. 更新成绩

**接口**: `PUT /api/teacher/grades/:gradeId`

**需要认证**: 是

**路径参数**:
- `gradeId`: 成绩ID

**请求参数**:
```json
{
  "regularScore": 85.0,
  "finalScore": 90.0,
  "totalScore": 88.0,
  "gradePoint": 4.0,
  "gradeLevel": "A"
}
```

**响应**:
```json
{
  "code": 200,
  "message": "更新成绩成功",
  "data": null
}
```

### 7. 获取教师课表

**接口**: `GET /api/teacher/schedule`

**需要认证**: 是

**查询参数**:
- `semesterId`: 学期ID（默认1）
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "courseId": 1,
        "courseCode": "CS101",
        "courseName": "计算机基础",
        "semesterId": 1,
        "semesterName": "2024-2025-第一学期",
        "selectedCount": 30
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 10
  }
}
```

## 管理员模块

### 1. 获取首页统计

**接口**: `GET /api/admin/home`

**需要认证**: 是

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "totalStudents": 1000,
    "totalTeachers": 100,
    "totalCourses": 200,
    "totalDepartments": 10,
    "totalMajors": 20,
    "totalClasses": 50
  }
}
```

### 2. 获取课程列表

**接口**: `GET /api/admin/courses`

**需要认证**: 是

**查询参数**:
- `keyword`: 搜索关键词（可选）
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "courseId": 1,
        "courseCode": "CS101",
        "courseName": "计算机基础",
        "courseTypeId": 1,
        "credit": 3.0,
        "totalHours": 48,
        "theoryHours": 32,
        "practiceHours": 16,
        "departmentId": 1,
        "majorId": 1,
        "isCompulsory": true,
        "statusId": 1
      }
    ],
    "total": 200,
    "page": 1,
    "pageSize": 10
  }
}
```

### 3. 创建课程

**接口**: `POST /api/admin/courses`

**需要认证**: 是

**请求参数**:
```json
{
  "courseCode": "CS101",
  "courseName": "计算机基础",
  "courseTypeId": 1,
  "credit": 3.0,
  "totalHours": 48,
  "theoryHours": 32,
  "practiceHours": 16,
  "departmentId": 1,
  "majorId": 1,
  "description": "计算机基础课程",
  "isCompulsory": true,
  "statusId": 1
}
```

**响应**:
```json
{
  "code": 200,
  "message": "创建课程成功",
  "data": {
    "courseId": 1,
    "courseCode": "CS101",
    "courseName": "计算机基础"
  }
}
```

### 4. 更新课程

**接口**: `PUT /api/admin/courses`

**需要认证**: 是

**请求参数**: 同创建课程

**响应**:
```json
{
  "code": 200,
  "message": "更新课程成功",
  "data": {}
}
```

### 5. 删除课程

**接口**: `DELETE /api/admin/courses/:courseId`

**需要认证**: 是

**路径参数**:
- `courseId`: 课程ID

**响应**:
```json
{
  "code": 200,
  "message": "删除课程成功",
  "data": null
}
```

### 6. 获取院系列表

**接口**: `GET /api/admin/departments`

**需要认证**: 是

**查询参数**:
- `keyword`: 搜索关键词（可选）
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "departmentId": 1,
        "departmentCode": "CS",
        "departmentName": "信息工程学院",
        "description": "计算机相关学院",
        "statusId": 1
      }
    ],
    "total": 10,
    "page": 1,
    "pageSize": 10
  }
}
```

### 7. 创建院系

**接口**: `POST /api/admin/departments`

**需要认证**: 是

**请求参数**:
```json
{
  "departmentCode": "CS",
  "departmentName": "信息工程学院",
  "description": "计算机相关学院",
  "statusId": 1
}
```

**响应**:
```json
{
  "code": 200,
  "message": "创建院系成功",
  "data": {
    "departmentId": 1,
    "departmentCode": "CS",
    "departmentName": "信息工程学院"
  }
}
```

### 8. 更新院系

**接口**: `PUT /api/admin/departments`

**需要认证**: 是

**请求参数**: 同创建院系

**响应**:
```json
{
  "code": 200,
  "message": "更新院系成功",
  "data": {}
}
```

### 9. 删除院系

**接口**: `DELETE /api/admin/departments/:deptId`

**需要认证**: 是

**路径参数**:
- `deptId`: 院系ID

**响应**:
```json
{
  "code": 200,
  "message": "删除院系成功",
  "data": null
}
```

### 10. 获取公告列表

**接口**: `GET /api/admin/announcements`

**需要认证**: 是

**查询参数**:
- `keyword`: 搜索关键词（可选）
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "announcementId": 1,
        "title": "关于期末考试安排的通知",
        "content": "期末考试将于...",
        "announcementType": 1,
        "publishDate": "2024-12-01",
        "expiryDate": "2024-12-31",
        "priority": 1,
        "statusId": 1
      }
    ],
    "total": 20,
    "page": 1,
    "pageSize": 10
  }
}
```

### 11. 创建公告

**接口**: `POST /api/admin/announcements`

**需要认证**: 是

**请求参数**:
```json
{
  "title": "关于期末考试安排的通知",
  "content": "期末考试将于...",
  "announcementType": 1,
  "publishDate": "2024-12-01",
  "expiryDate": "2024-12-31",
  "priority": 1,
  "statusId": 1
}
```

**响应**:
```json
{
  "code": 200,
  "message": "创建公告成功",
  "data": {
    "announcementId": 1,
    "title": "关于期末考试安排的通知"
  }
}
```

### 12. 更新公告

**接口**: `PUT /api/admin/announcements`

**需要认证**: 是

**请求参数**: 同创建公告

**响应**:
```json
{
  "code": 200,
  "message": "更新公告成功",
  "data": {}
}
```

### 13. 删除公告

**接口**: `DELETE /api/admin/announcements/:announcementId`

**需要认证**: 是

**路径参数**:
- `announcementId`: 公告ID

**响应**:
```json
{
  "code": 200,
  "message": "删除公告成功",
  "data": null
}
```

### 14. 获取成绩列表

**接口**: `GET /api/admin/grades`

**需要认证**: 是

**查询参数**:
- `keyword`: 搜索关键词（可选）
- `page`: 页码（默认1）
- `pageSize`: 每页数量（默认10）

**响应**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "gradeId": 1,
        "studentId": 1,
        "studentNo": "20240001",
        "studentName": "张三",
        "courseId": 1,
        "courseCode": "CS101",
        "courseName": "计算机基础",
        "semesterId": 1,
        "semesterName": "2024-2025-第一学期",
        "regularScore": 85.0,
        "finalScore": 90.0,
        "totalScore": 88.0,
        "gradePoint": 4.0,
        "gradeLevel": "A",
        "gradedAt": "2024-12-20 10:00:00"
      }
    ],
    "total": 1000,
    "page": 1,
    "pageSize": 10
  }
}
```

### 15. 创建成绩

**接口**: `POST /api/admin/grades`

**需要认证**: 是

**请求参数**:
```json
{
  "studentId": 1,
  "courseId": 1,
  "semesterId": 1,
  "regularScore": 85.0,
  "finalScore": 90.0,
  "totalScore": 88.0,
  "gradePoint": 4.0,
  "gradeLevel": "A",
  "statusId": 1
}
```

**响应**:
```json
{
  "code": 200,
  "message": "创建成绩成功",
  "data": {
    "gradeId": 1,
    "studentId": 1,
    "courseId": 1
  }
}
```

### 16. 更新成绩

**接口**: `PUT /api/admin/grades`

**需要认证**: 是

**请求参数**: 同创建成绩

**响应**:
```json
{
  "code": 200,
  "message": "更新成绩成功",
  "data": {}
}
```

### 17. 删除成绩

**接口**: `DELETE /api/admin/grades/:gradeId`

**需要认证**: 是

**路径参数**:
- `gradeId`: 成绩ID

**响应**:
```json
{
  "code": 200,
  "message": "删除成绩成功",
  "data": null
}
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 401 | 未授权/Token无效 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 数据库表结构

### users (用户表)
- user_id (主键)
- username
- password
- phone
- email
- avatar
- real_name
- gender_id
- birthday
- address
- user_type_id
- status_id
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### students (学生表)
- student_id (主键)
- student_no
- user_id (外键)
- department_id
- major_id
- class_id
- enrollment_date
- graduation_date
- education_years
- education_level_id
- status_id
- emergency_contact
- emergency_phone
- parent_name
- parent_phone
- home_address
- high_school
- entrance_score
- scholarship_info
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### teachers (教师表)
- teacher_id (主键)
- teacher_no
- user_id (外键)
- department_id
- title_id
- hire_date
- office_location
- phone
- email
- research_direction
- education_background
- work_experience
- status_id
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### courses (课程表)
- course_id (主键)
- course_code
- course_name
- course_type_id
- credit
- total_hours
- theory_hours
- practice_hours
- department_id
- major_id
- description
- syllabus
- textbook
- prerequisites
- is_compulsory
- status_id
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### course_selections (选课表)
- selection_id (主键)
- student_id (外键)
- course_id (外键)
- semester_id (外键)
- selection_type_id
- status_id
- selection_date
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### course_grades (成绩表)
- grade_id (主键)
- student_id (外键)
- course_id (外键)
- semester_id (外键)
- regular_score
- final_score
- total_score
- grade_point
- grade_level
- status_id
- graded_at
- graded_by
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### teaching_assignments (教学安排表)
- assignment_id (主键)
- teacher_id (外键)
- course_id (外键)
- semester_id (外键)
- status_id
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### departments (院系表)
- department_id (主键)
- department_code
- department_name
- description
- status_id
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### majors (专业表)
- major_id (主键)
- major_code
- major_name
- department_id (外键)
- description
- status_id
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### classes (班级表)
- class_id (主键)
- class_code
- class_name
- department_id (外键)
- major_id (外键)
- enrollment_year
- status_id
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### semesters (学期表)
- semester_id (主键)
- semester_code
- semester_name
- semester_type_id
- start_date
- end_date
- is_current
- status_id
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

### announcements (公告表)
- announcement_id (主键)
- title
- content
- announcement_type
- publish_date
- expiry_date
- priority
- status_id
- created_by
- updated_by
- created_at
- updated_at
- deleted_at

## 开发规范

### 代码风格

- 遵循 Go 官方代码风格指南
- 使用 `gofmt` 格式化代码
- 使用 `golint` 检查代码

### Git 提交规范

```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 重构
test: 测试相关
chore: 构建/工具链相关
```

## 测试

### 运行单元测试

```bash
go test ./...
```

### 运行特定包的测试

```bash
go test ./utils
```

### 查看测试覆盖率

```bash
go test -cover ./...
```

## 部署

### Docker 部署

```bash
# 构建镜像
docker build -t lucky-sms-backend .

# 运行容器
docker run -p 8081:8081 --env-file .env lucky-sms-backend
```

### 二进制部署

```bash
# 编译
go build -o lucky-sms main.go

# 运行
./lucky-sms
```

## 常见问题

### 1. 数据库连接失败

检查 `config.yaml` 中的数据库配置是否正确，确保 MySQL 服务已启动。

### 2. JWT Token 无效

检查 `config.yaml` 中的 JWT secret 配置，确保前后端使用相同的 secret。

### 3. CORS 错误

检查 `config.yaml` 中的 CORS 配置，确保前端地址在允许的源列表中。

## 联系方式

如有问题，请联系开发团队。
