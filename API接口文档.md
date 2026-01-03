# Lucky SMS 前后端接口文档

## 文档说明

本文档详细描述了Lucky SMS学生管理系统的所有前后端接口，确保与项目实际代码完全一致。

**更新时间**: 2026-01-03

---

## 通用说明

### 请求格式

所有接口返回统一格式：

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {}
}
```

**code说明**:
- `200`: 请求成功
- `401`: 未登录
- `500`: 服务器错误

### 认证方式

所有需要认证的接口使用JWT Token，在请求头中携带：
```
Authorization: Bearer {token}
```

---

## 一、登录注册模块

### 1.1 手机号验证码登录

**接口地址**: `POST /login/phone`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | String | 是 | 手机号 |
| captcha | String | 是 | 验证码（测试环境固定为"123456"） |

**返回示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 1,
    "username": "admin",
    "realName": "管理员",
    "role": "ADMIN",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### 1.2 手机号密码登录

**接口地址**: `POST /login/password`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | String | 是 | 手机号 |
| password | String | 是 | 密码 |

**返回示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "userId": 1,
    "username": "admin",
    "realName": "管理员",
    "role": "ADMIN",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### 1.3 重置密码

**接口地址**: `POST /resetPassword`

**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | String | 是 | 手机号 |
| captcha | String | 是 | 验证码（测试环境固定为"123456"） |
| newPassword | String | 是 | 新密码 |

**返回示例**:
```json
{
  "code": 200,
  "message": "重置成功",
  "data": null
}
```

---

## 二、管理员模块 (Admin)

### 2.1 用户管理

#### 2.2.1 获取用户列表

**接口地址**: `GET /admin/users`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| keyword | String | 否 | - | 搜索关键词 |
| role | String | 否 | - | 角色筛选 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": 1,
        "username": "student001",
        "realName": "张三",
        "role": "STUDENT",
        "department": "计算机学院",
        "email": "zhangsan@example.com",
        "phone": "13800138000",
        "status": "ACTIVE",
        "createTime": "2024-01-01 10:00:00"
      }
    ],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

#### 2.2.2 添加用户

**接口地址**: `POST /admin/users`

**请求参数**:
```json
{
  "username": "student002",
  "password": "123456",
  "phone": "13800138001",
  "email": "lisi@example.com",
  "realName": "李四",
  "userTypeId": 1
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "添加用户成功",
  "data": {
    "userId": 101
  }
}
```

#### 2.2.3 更新用户

**接口地址**: `PUT /admin/users`

**请求参数**:
```json
{
  "userId": 1,
  "username": "student001",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "realName": "张三",
  "password": "newpassword"
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "更新用户成功",
  "data": {}
}
```

#### 2.1.4 删除用户

**接口地址**: `DELETE /admin/users/{userId}`

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| userId | Integer | 用户ID |

**返回示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": true
}
```

### 2.2 课程管理

#### 2.2.1 获取课程列表

**接口地址**: `GET /admin/courses`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| keyword | String | 否 | - | 搜索关键词 |
| department | String | 否 | - | 部门筛选 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": 1,
        "courseCode": "CS1001",
        "courseName": "高等数学",
        "courseType": "必修课",
        "department": "计算机学院",
        "teacherName": "王老师",
        "credits": 4,
        "hours": 64,
        "semester": "2024-2025学年第一学期",
        "enrolledCount": 30,
        "maxCapacity": 60,
        "status": "OPEN"
      }
    ],
    "total": 50,
    "size": 10,
    "current": 1,
    "pages": 5
  }
}
```

#### 2.3.2 添加课程

**接口地址**: `POST /admin/courses`

**请求参数**:
```json
{
  "courseCode": "CS1002",
  "courseName": "线性代数",
  "courseTypeId": 1,
  "departmentId": 1,
  "credit": 3.5,
  "totalHours": 56,
  "description": "线性代数课程"
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "添加课程成功",
  "data": {
    "courseId": 51
  }
}
```

#### 2.3.3 更新课程

**接口地址**: `PUT /admin/courses`

**请求参数**:
```json
{
  "courseId": 1,
  "courseCode": "CS1001",
  "courseName": "高等数学",
  "courseTypeId": 1,
  "departmentId": 1,
  "credit": 4,
  "totalHours": 64,
  "description": "高等数学课程"
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "更新课程成功",
  "data": {}
}
```

#### 2.2.4 删除课程

**接口地址**: `DELETE /admin/courses/{courseId}`

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| courseId | Integer | 课程ID |

**返回示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": true
}
```

### 2.3 成绩管理

#### 2.3.1 获取成绩列表

**接口地址**: `GET /admin/grades`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| keyword | String | 否 | - | 搜索关键词 |
| semester | String | 否 | - | 学期筛选 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": 1,
        "studentId": "20210001",
        "studentName": "张三",
        "className": "计科2101班",
        "courseCode": "CS1001",
        "courseName": "高等数学",
        "teacherName": "王老师",
        "semester": "2024-2025学年第一学期",
        "score": 85.5,
        "grade": "B",
        "status": "PUBLISHED"
      }
    ],
    "total": 500,
    "size": 10,
    "current": 1,
    "pages": 50
  }
}
```

#### 2.4.2 更新成绩

**接口地址**: `PUT /admin/grades`

**请求参数**:
```json
{
  "gradeId": 1,
  "score": 90.0,
  "grade": "A"
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "更新成绩成功",
  "data": {}
}
```

#### 2.4.3 删除成绩

**接口地址**: `DELETE /admin/grades/{gradeId}`

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| gradeId | Integer | 成绩ID |

**返回示例**:
```json
{
  "code": 200,
  "message": "删除成绩成功",
  "data": true
}
```

### 2.4 部门管理

#### 2.4.1 获取部门列表

**接口地址**: `GET /admin/departments`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| keyword | String | 否 | - | 搜索关键词 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": 1,
        "deptCode": "CS",
        "deptName": "计算机学院",
        "deanName": "院长1",
        "phone": "010-12345678",
        "email": "cs@example.com",
        "teacherCount": 15,
        "studentCount": 300,
        "status": "ACTIVE"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

#### 2.5.2 添加部门

**接口地址**: `POST /admin/departments`

**请求参数**:
```json
{
  "departmentCode": "CS",
  "departmentName": "计算机学院",
  "phone": "010-12345678",
  "email": "cs@example.com",
  "description": "计算机学院"
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "添加部门成功",
  "data": {
    "departmentId": 6
  }
}
```

#### 2.4.3 更新部门

**接口地址**: `PUT /admin/departments`

**请求参数**:
```json
{
  "departmentId": 1,
  "departmentCode": "CS",
  "departmentName": "计算机学院",
  "phone": "010-12345678",
  "email": "cs@example.com",
  "description": "计算机学院"
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "更新部门成功",
  "data": {}
}
```

#### 2.5.4 删除部门

**接口地址**: `DELETE /admin/departments/{deptId}`

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| deptId | Integer | 部门ID |

**返回示例**:
```json
{
  "code": 200,
  "message": "删除成功",
  "data": true
}
```

### 2.5 公告管理

#### 2.5.1 获取公告列表

**接口地址**: `GET /admin/announcements`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| keyword | String | 否 | - | 搜索关键词 |
| type | String | 否 | - | 公告类型 |
| department | String | 否 | - | 部门筛选 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "list": [
      {
        "announcementId": 1,
        "title": "关于2024-2025学年第一学期教学安排的通知",
        "content": "通知内容...",
        "publisherId": 1,
        "publisherName": "管理员",
        "departmentId": 1,
        "departmentName": "教务处",
        "announcementTypeId": 1,
        "announcementTypeName": "教学通知",
        "priority": 1,
        "targetAudience": "ALL",
        "statusId": 1,
        "publishTime": "2024-01-01 10:00:00",
        "createTime": "2024-01-01 09:00:00"
      }
    ],
    "total": 20,
    "page": 1,
    "size": 10,
    "message": "查询成功"
  }
}
```

#### 2.6.2 创建公告

**接口地址**: `POST /admin/announcements`

**请求参数**:
```json
{
  "title": "关于考试安排的通知",
  "content": "考试安排详情...",
  "publisherId": 1,
  "departmentId": 1,
  "announcementTypeId": 1,
  "priority": 1,
  "targetAudience": "ALL",
  "statusId": 1
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "公告创建成功",
  "data": {
    "announcementId": 21
  }
}
```

#### 2.6.3 更新公告

**接口地址**: `PUT /admin/announcements`

**请求参数**:
```json
{
  "announcementId": 1,
  "title": "关于考试安排的通知（更新）",
  "content": "考试安排详情...",
  "departmentId": 1,
  "announcementTypeId": 1,
  "priority": 1,
  "targetAudience": "ALL",
  "statusId": 1
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "公告更新成功",
  "data": {}
}
```

#### 2.5.4 删除公告

**接口地址**: `DELETE /admin/announcements/{announcementId}`

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| announcementId | Integer | 公告ID |

**返回示例**:
```json
{
  "code": 200,
  "message": "公告删除成功",
  "data": true
}
```

### 2.6 教师管理

#### 2.6.1 获取教师列表

**接口地址**: `GET /admin/teachers`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| keyword | String | 否 | - | 搜索关键词 |

**返回示例**:
```json
{
  "code": 200,
  "message": "获取教师列表成功",
  "data": {}
}
```

---

## 三、学生模块 (Student)

### 3.1 首页数据

**接口地址**: `GET /student/home`

**请求参数**: 无

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "studentName": "张三",
    "studentId": "20210001",
    "className": "计科2101班",
    "major": "计算机科学与技术",
    "department": "计算机学院",
    "semester": "2024-2025学年第一学期",
    "gpa": 3.5,
    "totalCredits": 120,
    "enrolledCourses": 8,
    "upcomingExams": [],
    "recentGrades": [],
    "announcements": []
  }
}
```

### 3.2 成绩查询

#### 3.2.1 获取所有成绩

**接口地址**: `GET /student/grades`

**请求参数**: 无

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "grades": [
      {
        "courseId": 1,
        "courseName": "高等数学",
        "courseCode": "CS1001",
        "credit": 4,
        "score": 85.5,
        "grade": "B",
        "semester": "2024-2025学年第一学期",
        "teacherName": "王老师"
      }
    ],
    "gpa": 3.5,
    "totalCredits": 120
  }
}
```

#### 3.3.2 按学期获取成绩

**接口地址**: `GET /student/grades/{semester}`

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| semester | String | 学期名称 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "grades": [
      {
        "courseId": 1,
        "courseName": "高等数学",
        "courseCode": "CS1001",
        "credit": 4,
        "score": 85.5,
        "grade": "B",
        "semester": "2024-2025学年第一学期",
        "teacherName": "王老师"
      }
    ],
    "gpa": 3.5,
    "totalCredits": 120
  }
}
```

#### 3.3.3 获取成绩（分页）

**接口地址**: `GET /student/grades/pagination`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "grades": [],
    "gpa": 3.5,
    "totalCredits": 120
  }
}
```

#### 3.3.4 按学期获取成绩（分页）

**接口地址**: `GET /student/grades/{semester}/pagination`

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| semester | String | 学期名称 |

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "grades": [],
    "gpa": 3.5,
    "totalCredits": 120
  }
}
```

### 3.3 选课系统

#### 3.3.1 获取可选课程（分页）

**接口地址**: `GET /student/courses/available/pagination`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| semesterId | Integer | 否 | 1 | 学期ID |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**返回示例**:
```json
{
  "code": 200,
  "message": "获取课程列表成功",
  "data": {
    "records": [],
    "total": 0,
    "size": 10,
    "current": 1,
    "pages": 0
  }
}
```

#### 3.3.2 获取已选课程（分页）

**接口地址**: `GET /student/courses/selected/pagination`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| semesterId | Integer | 否 | 1 | 学期ID |
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [],
    "total": 0,
    "size": 10,
    "current": 1,
    "pages": 0
  }
}
```

#### 3.4.5 选课

**接口地址**: `POST /student/courses/select`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| assignmentId | Integer | 是 | - | 教学安排ID |
| selectionTypeId | Integer | 否 | 1 | 选课类型ID |

**返回示例**:
```json
{
  "code": 200,
  "message": "选课成功",
  "data": {
    "selectionId": 101,
    "message": "选课成功"
  }
}
```

#### 3.4.6 退课

**接口地址**: `POST /student/courses/drop`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| selectionId | Integer | 是 | - | 选课记录ID |
| dropReason | String | 否 | 个人原因 | 退课原因 |

**返回示例**:
```json
{
  "code": 200,
  "message": "退课成功",
  "data": {
    "selectionId": 101,
    "message": "退课成功"
  }
}
```

### 3.4 个人信息

#### 3.4.1 获取个人信息

**接口地址**: `GET /student/profile`

**请求参数**: 无

**返回示例**:
```json
{
  "code": 200,
  "message": "获取个人信息成功",
  "data": {
    "studentId": "20210001",
    "studentName": "张三",
    "gender": "男",
    "birthDate": "2002-05-15",
    "phone": "13800138000",
    "email": "zhangsan@example.com",
    "className": "计科2101班",
    "major": "计算机科学与技术",
    "department": "计算机学院"
  }
}
```

#### 3.5.2 更新个人信息

**接口地址**: `POST /student/profile`

**请求参数**:
```json
{
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "address": "北京市海淀区"
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "更新个人信息成功",
  "data": null
}
```

### 3.5 课表查询

**接口地址**: `GET /student/schedule`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| semester | String | 否 | 2024-2025-第一学期 | 学期 |

**返回示例**:
```json
{
  "code": 200,
  "message": "获取课表成功",
  "data": {
    "semester": "2024-2025-第一学期",
    "schedule": [
      {
        "dayOfWeek": 1,
        "period": 1,
        "courseName": "高等数学",
        "teacherName": "王老师",
        "classroom": "A101",
        "weeks": "1-16周"
      }
    ]
  }
}
```

### 3.7 公告管理

#### 3.7.1 获取公告列表

**接口地址**: `GET /student/announcements`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |

**返回示例**:
```json
{
  "code": 200,
  "message": "获取公告成功",
  "data": [
    {
      "announcementId": 1,
      "title": "关于2024-2025学年第一学期教学安排的通知",
      "content": "通知内容...",
      "publisherName": "教务处",
      "publishTime": "2024-01-01 10:00:00",
      "isRead": false
    }
  ]
}
```

#### 3.7.2 标记公告已读

**接口地址**: `POST /student/announcements/{announcementId}/read`

**路径参数**:
| 参数名 | 类型 | 说明 |
|--------|------|------|
| announcementId | Integer | 公告ID |

**返回示例**:
```json
{
  "code": 200,
  "message": "标记已读成功",
  "data": true
}
```

---

## 四、教师模块 (Teacher)

### 4.1 首页数据

**接口地址**: `GET /teacher/home`

**请求参数**: 无

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "teacherName": "王老师",
    "teacherId": "T001",
    "department": "计算机学院",
    "title": "教授",
    "semester": "2024-2025学年第一学期",
    "totalCourses": 5,
    "totalStudents": 150,
    "pendingGrades": 20,
    "upcomingClasses": [],
    "recentActivities": []
  }
}
```

### 4.2 学生管理

#### 4.2.1 获取学生列表

**接口地址**: `GET /teacher/students`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| keyword | String | 否 | - | 搜索关键词 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "studentId": "20210001",
        "studentName": "张三",
        "className": "计科2101班",
        "major": "计算机科学与技术",
        "phone": "13800138000",
        "email": "zhangsan@example.com"
      }
    ],
    "total": 50,
    "size": 10,
    "current": 1,
    "pages": 5
  }
}
```

### 4.2 课程管理

#### 4.2.1 获取课程列表

**接口地址**: `GET /teacher/courses`

**请求参数**:
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | Integer | 否 | 1 | 页码 |
| size | Integer | 否 | 10 | 每页数量 |
| semester | String | 否 | 2024-2025-第一学期 | 学期 |

**返回示例**:
```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "courseId": 1,
        "courseName": "高等数学",
        "courseCode": "CS1001",
        "credit": 4,
        "hours": 64,
        "classroom": "A101",
        "schedule": "周一 1-2节",
        "enrolledCount": 30,
        "maxCapacity": 60,
        "status": "进行中"
      }
    ],
    "total": 5,
    "size": 10,
    "current": 1,
    "pages": 1
  }
}
```

**文档结束**

---

## 五、前端API调用说明

### 5.1 管理员API (admin.js)

```javascript
// 首页数据
getAdminHomeData()

// 用户管理
getUserList(params)
addUser(data)
updateUser(data)
deleteUser(userId)
resetUserPassword(userId)
batchOperateUsers(data)

// 课程管理
getCourseList(params)
addCourse(data)
updateCourse(data)
deleteCourse(courseId)
getCourseStudents(courseId)
batchOperateCourses(data)

// 成绩管理
getGradeList(params)
updateGrade(data)
deleteGrade(gradeId)
importGrades(data)

// 部门管理
getDepartmentList(params)
addDepartment(data)
updateDepartment(data)
deleteDepartment(departmentId)

// 公告管理
getAnnouncementList(params)
createAnnouncement(data)
updateAnnouncement(data)
deleteAnnouncement(announcementId)

// 系统设置
getSystemSettings()
updateSystemSettings(data)

// 统计数据
getStatistics(params)

// 教师管理
getTeacherList()
```

### 5.2 学生API (student.js)

```javascript
// 首页数据
getHomeData()

// 学籍信息
getStudentStatus()

// 成绩查询
getGrades()
getGradesBySemester(semester)
getGradesWithPagination(params)
getGradesBySemesterWithPagination(semester, params)

// 选课系统
getAvailableCourses()
getAvailableCoursesWithPagination(params)
getSelectedCourses()
getSelectedCoursesWithPagination(params)
selectCourse(assignmentId)
dropCourse(selectionId)

// 个人信息
getStudentProfile()
updateStudentProfile(data)

// 课表查询
getStudentSchedule(params)

// 公告管理
getStudentAnnouncements(params)
markAnnouncementRead(announcementId)
```

### 5.3 教师API (teacher.js)

```javascript
// 首页数据
getHomeData()

// 学生管理
getStudentsList(params)

// 课程管理
getCoursesList(params)

// 成绩管理
getStudentGrades(params)
updateStudentGrade(data)

// 个人信息
getTeacherProfile()
updateTeacherProfile(data)

// 教学计划
getSchedule(params)
```

---

## 六、接口状态说明

### 6.1 已实现接口

所有接口均已实现，包括：
- 登录注册模块：3个接口
- 管理员模块：32个接口
- 学生模块：17个接口
- 教师模块：8个接口

### 6.2 前端页面状态

**已实现页面**：
- 管理员：首页、用户管理、课程管理、成绩管理、部门管理、公告管理
- 学生：首页、学籍信息、成绩查询、选课系统、课表查询、个人信息
- 教师：首页、课程管理、成绩管理、学生管理、教学计划、个人信息

### 6.3 数据库表结构

所有数据库表已创建，包括22个核心表：
- 用户相关：users, students, teachers
- 组织架构：departments, majors, classes
- 课程相关：courses, semesters, teaching_assignments, course_selections, course_grades
- 公告相关：announcements, announcement_types, announcement_reads
- 基础数据：status_types, course_types, selection_types, titles, gender_types, user_types, semester_types

---

## 七、注意事项

1. **认证要求**：除登录注册接口外，所有接口都需要JWT Token认证
2. **分页参数**：page从1开始，size默认为10
3. **时间格式**：统一使用 `yyyy-MM-dd HH:mm:ss` 格式
4. **日期格式**：使用 `yyyy-MM-dd` 格式
5. **错误处理**：所有接口统一返回错误码和错误信息
6. **测试环境**：验证码固定为 "123456"

---

## 八、更新日志

| 日期 | 版本 | 更新内容 |
|------|------|----------|
| 2026-01-03 | 1.0 | 初始版本，完整记录所有接口 |

---

**文档结束**
