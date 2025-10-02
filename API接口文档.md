# Lucky SMS 学生管理系统 API 接口文档

## 文档说明

本文档为 Lucky SMS 学生管理系统的后端 API 接口文档，用于指导后端开发人员实现系统所需的所有接口。前端项目使用 Vue.js 框架开发，所有数据均需通过后端 API 获取。

## 基础信息

- **基础URL**: `http://localhost:8081/api/`
- **认证方式**: Bearer Token (JWT)
- **数据格式**: JSON
- **响应格式**: 统一返回格式

### 统一响应格式

```json
{
  "code": 200,      // 状态码，200表示成功，其他表示失败
  "message": "操作成功", // 提示信息
  "data": {}        // 返回的数据
}
```

### 状态码说明

| 状态码 | 说明      |
|--------|---------|
| 200 | 请求成功    |
| 400 | 请求参数错误  |
| 401 | 未授权     |
| 403 | 服务器拒绝 |
| 404 | 资源不存在   |
| 500 | 服务器内部错误 |

## 1. 认证相关接口

### 1.1 用户登录

**接口地址**: `POST /login`

**请求参数**:

```json
{
  "username": "string",  // 用户名
  "keyhash": "string"    // 密码
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": "string",
      "username": "string",
      "name": "string",
      "role": "string",  // admin, student, teacher
      "avatar": "string"
    }
  }
}
```

### 1.2 手机号登录

**接口地址**: `POST /login/phone`

**请求参数**:

```json
{
  "phone": "string",   // 手机号
  "captcha": "string"  // 验证码
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": "string",
      "username": "string",
      "name": "string",
      "role": "string",  // admin, student, teacher
      "avatar": "string"
    }
  }
}
```

### 1.3 发送验证码

**接口地址**: `POST /captcha/send`

**请求参数**:

```json
{
  "phone": "string"   // 手机号
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "验证码发送成功",
  "data": null
}
```

### 1.4 用户注册（注册和手机号登录共用接口）

**接口地址**: `POST /register`

**请求参数**:

```json
{
  "username": "string",  // 用户名
  "phone": "string",     // 手机号
  "captcha": "string",   // 验证码
  "password": "string"   // 密码（重置密码时需要）
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "注册成功",
  "data": null
}
```

## 2. 学生相关接口

### 2.1 学生首页数据

**接口地址**: `GET /student/home`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "student": {
      "id": "string",
      "name": "string",
      "class": "string",
      "gpa": "string",
      "rank": number,
      "classSize": number,
      "courseCount": number,
      "nextCourse": {
        "name": "string",
        "time": "string",
        "location": "string"
      },
      "todos": [
        {
          "id": number,
          "text": "string",
          "dueDate": "string",
          "completed": boolean,
          "important": boolean
        }
      ]
    },
    "announcements": [
      {
        "id": number,
        "title": "string",
        "date": "string",
        "department": "string",
        "type": "string",  // important, notice, info, activity
        "content": "string"
      }
    ]
  }
}
```

### 2.2 学生成绩查询

**接口地址**: `GET /student/grades`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semester | string | 否 | 学期，如 "2023-2024-2" |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "grades": [
      {
        "id": number,
        "courseName": "string",
        "courseCode": "string",
        "courseType": "string",
        "credits": number,
        "score": number,
        "gpa": number,
        "semester": "string"
      }
    ],
    "total": number,
    "overview": {
      "gpa": "string",
      "avgScore": number,
      "rank": number,
      "classSize": number,
      "completedCredits": number,
      "totalCredits": number,
      "scoreDistribution": [
        {
          "label": "string",
          "percentage": number,
          "color": "string"
        }
      ]
    }
  }
}
```

### 2.3 学生课表查询

**接口地址**: `GET /student/schedule`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semester | string | 否 | 学期，如 "2023-2024-2" |

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "currentCourses": [
      {
        "id": number,
        "name": "string",
        "code": "string",
        "teacher": "string",
        "credits": number,
        "progress": number,
        "schedule": [
          {
            "day": "string",
            "time": "string",
            "location": "string"
          }
        ],
        "color": "string"
      }
    ],
    "historyCourses": [
      {
        "semester": "string",
        "name": "string",
        "code": "string",
        "teacher": "string",
        "credits": number,
        "score": number,
        "status": "string"  // passed, failed, retaking, auditing
      }
    ],
    "stats": {
      "currentCourseCount": number,
      "completedCourseCount": number,
      "failedCourseCount": number,
      "avgScore": number
    }
  }
}
```

### 2.4 学生学籍信息

**接口地址**: `GET /student/status`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "basicInfo": {
      "id": "string",
      "name": "string",
      "gender": "string",
      "birthDate": "string",
      "idCard": "string",
      "nation": "string",
      "politicalStatus": "string",
      "enrollmentDate": "string",
      "educationLevel": "string",
      "major": "string",
      "class": "string",
      "department": "string",
      "advisor": "string",
      "phone": "string",
      "email": "string",
      "address": "string"
    },
    "academicStatus": {
      "status": "string",
      "gpa": "string",
      "totalCredits": number,
      "completedCredits": number,
      "failedCourses": number
    }
  }
}
```

### 2.5 学生选课系统

**接口地址**: `GET /student/course`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "availableCourses": [
      {
        "id": number,
        "name": "string",
        "code": "string",
        "teacher": "string",
        "credits": number,
        "time": "string",
        "location": "string",
        "capacity": number,
        "selected": number,
        "description": "string"
      }
    ],
    "selectedCourses": [
      {
        "id": number,
        "name": "string",
        "code": "string",
        "teacher": "string",
        "credits": number,
        "time": "string",
        "location": "string"
      }
    ]
  }
}
```

### 2.6 选课操作

**接口地址**: `POST /student/course/select`

**请求参数**:

```json
{
  "courseId": number  // 课程ID
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "选课成功",
  "data": null
}
```

### 2.7 退课操作

**接口地址**: `POST /student/course/drop`

**请求参数**:

```json
{
  "courseId": number  // 课程ID
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "退课成功",
  "data": null
}
```

### 2.8 图书借阅信息

**接口地址**: `GET /student/library`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | string | 否 | 状态：borrowed(借阅中), returned(已归还), overdue(已逾期) |

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "borrowedBooks": [
      {
        "id": number,
        "title": "string",
        "author": "string",
        "isbn": "string",
        "borrowDate": "string",
        "dueDate": "string",
        "status": "string"  // borrowed, overdue
      }
    ],
    "historyBooks": [
      {
        "id": number,
        "title": "string",
        "author": "string",
        "isbn": "string",
        "borrowDate": "string",
        "returnDate": "string"
      }
    ],
    "stats": {
      "totalBorrowed": number,
      "currentBorrowed": number,
      "overdueCount": number
    }
  }
}
```

### 2.9 更新待办事项状态

**接口地址**: `PUT /student/todo/{id}`

**请求参数**:

```json
{
  "completed": boolean  // 是否完成
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

## 3. 教师相关接口

### 3.1 教师首页数据

**接口地址**: `GET /teacher/home`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "teacher": {
      "id": "string",
      "name": "string",
      "department": "string",
      "title": "string",
      "teachingYears": number,
      "online": boolean,
      "avatar": "string",
      "courseCount": number,
      "studentCount": number,
      "avgScore": number,
      "attendanceRate": number,
      "nextClass": {
        "id": "string",
        "name": "string",
        "time": "string",
        "location": "string",
        "studentCount": number
      },
      "courses": [
        {
          "id": "string",
          "name": "string"
        }
      ],
      "tasks": [
        {
          "id": number,
          "text": "string",
          "dueDate": "string",
          "completed": boolean,
          "important": boolean,
          "course": "string"
        }
      ]
    },
    "announcements": [
      {
        "id": number,
        "title": "string",
        "date": "string",
        "department": "string",
        "type": "string",  // important, notice, info
        "content": "string"
      }
    ]
  }
}
```

### 3.2 教师课程管理

**接口地址**: `GET /teacher/courses`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semester | string | 否 | 学期，如 "2023-2024-2" |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |
| search | string | 否 | 搜索关键词 |
| category | string | 否 | 课程分类 |

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "courses": [
      {
        "id": number,
        "name": "string",
        "code": "string",
        "teacher": "string",
        "studentCount": number,
        "time": "string",
        "location": "string",
        "credits": number,
        "category": "string",
        "status": number,  // 1:未开始, 2:进行中, 3:已结束
        "schedule": [
          {
            "day": number,
            "timeSlot": number
          }
        ]
      }
    ],
    "total": number,
    "categories": [
      {
        "value": "string",
        "label": "string"
      }
    ],
    "semesters": [
      {
        "value": "string",
        "label": "string"
      }
    ]
  }
}
```

### 3.3 创建课程

**接口地址**: `POST /teacher/course`

**请求参数**:

```json
{
  "name": "string",
  "code": "string",
  "category": "string",
  "credits": number,
  "time": "string",
  "location": "string",
  "status": number
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": number
  }
}
```

### 3.4 更新课程

**接口地址**: `PUT /teacher/course/{id}`

**请求参数**:

```json
{
  "name": "string",
  "code": "string",
  "category": "string",
  "credits": number,
  "time": "string",
  "location": "string",
  "status": number
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

### 3.5 删除课程

**接口地址**: `DELETE /teacher/course/{id}`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 3.6 教师成绩管理

**接口地址**: `GET /teacher/grades`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| courseId | number | 否 | 课程ID |
| classId | number | 否 | 班级ID |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "grades": [
      {
        "id": number,
        "studentId": "string",
        "studentName": "string",
        "className": "string",
        "courseId": number,
        "courseName": "string",
        "usualScore": number,
        "examScore": number,
        "totalScore": number,
        "gpa": number,
        "semester": "string"
      }
    ],
    "total": number,
    "courses": [
      {
        "id": number,
        "name": "string"
      }
    ],
    "classes": [
      {
        "id": number,
        "name": "string"
      }
    ]
  }
}
```

### 3.7 录入成绩

**接口地址**: `POST /teacher/grade`

**请求参数**:

```json
{
  "studentId": "string",
  "courseId": number,
  "usualScore": number,
  "examScore": number,
  "totalScore": number,
  "semester": "string"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "录入成功",
  "data": null
}
```

### 3.8 批量导入成绩

**接口地址**: `POST /teacher/grades/import`

**请求参数**: 文件上传（Excel格式）

**响应示例**:

```json
{
  "code": 200,
  "message": "导入成功",
  "data": {
    "successCount": number,
    "failCount": number,
    "failList": [
      {
        "row": number,
        "reason": "string"
      }
    ]
  }
}
```

### 3.9 教师学生管理

**接口地址**: `GET /teacher/students`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| courseId | number | 否 | 课程ID |
| classId | number | 否 | 班级ID |
| search | string | 否 | 搜索关键词 |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "students": [
      {
        "id": "string",
        "name": "string",
        "gender": "string",
        "className": "string",
        "phone": "string",
        "email": "string",
        "enrollmentDate": "string",
        "status": "string"
      }
    ],
    "total": number,
    "courses": [
      {
        "id": number,
        "name": "string"
      }
    ],
    "classes": [
      {
        "id": number,
        "name": "string"
      }
    ]
  }
}
```

### 3.10 教师教学计划

**接口地址**: `GET /teacher/schedule`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semester | string | 否 | 学期，如 "2023-2024-2" |

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "schedule": [
      {
        "day": "string",
        "timeSlots": [
          {
            "time": "string",
            "course": {
              "id": number,
              "name": "string",
              "location": "string",
              "class": "string"
            }
          }
        ]
      }
    ],
    "semesters": [
      {
        "value": "string",
        "label": "string"
      }
    ]
  }
}
```

### 3.11 教师师生交流

**接口地址**: `GET /teacher/communication`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | string | 否 | 消息类型：all(全部), unread(未读), sent(已发送) |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "messages": [
      {
        "id": number,
        "senderId": "string",
        "senderName": "string",
        "receiverId": "string",
        "receiverName": "string",
        "content": "string",
        "sendTime": "string",
        "readTime": "string",
        "type": "string"  // received, sent
      }
    ],
    "total": number,
    "unreadCount": number
  }
}
```

### 3.12 发送消息

**接口地址**: `POST /teacher/message`

**请求参数**:

```json
{
  "receiverId": "string",
  "content": "string"
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "发送成功",
  "data": null
}
```

### 3.13 标记消息已读

**接口地址**: `PUT /teacher/message/{id}/read`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

### 3.14 更新教师任务状态

**接口地址**: `PUT /teacher/task/{id}`

**请求参数**:

```json
{
  "completed": boolean  // 是否完成
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": null
}
```

## 4. 公共接口

### 4.1 获取学期列表

**接口地址**: `GET /common/semesters`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "value": "2023-2024-2",
      "label": "2023-2024学年第二学期"
    },
    {
      "value": "2023-2024-1",
      "label": "2023-2024学年第一学期"
    }
  ]
}
```

### 4.2 获取公告列表

**接口地址**: `GET /common/announcements`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| type | string | 否 | 公告类型：all(全部), important(重要), notice(通知), info(资讯), activity(活动) |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "announcements": [
      {
        "id": number,
        "title": "string",
        "content": "string",
        "date": "string",
        "department": "string",
        "type": "string"  // important, notice, info, activity
      }
    ],
    "total": number
  }
}
```

### 4.3 获取公告详情

**接口地址**: `GET /common/announcement/{id}`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": number,
    "title": "string",
    "content": "string",
    "date": "string",
    "department": "string",
    "type": "string",  // important, notice, info, activity
    "attachments": [
      {
        "id": number,
        "name": "string",
        "url": "string",
        "size": number
      }
    ]
  }
}
```

## 5. 数据模型说明

### 5.1 用户模型

```json
{
  "id": "string",
  "username": "string",
  "password": "string",
  "name": "string",
  "role": "string",  // admin, student, teacher
  "avatar": "string",
  "phone": "string",
  "email": "string",
  "status": "string",
  "createTime": "string",
  "updateTime": "string"
}
```

### 5.2 学生模型

```json
{
  "id": "string",
  "userId": "string",
  "name": "string",
  "gender": "string",
  "birthDate": "string",
  "idCard": "string",
  "nation": "string",
  "politicalStatus": "string",
  "enrollmentDate": "string",
  "educationLevel": "string",
  "major": "string",
  "class": "string",
  "department": "string",
  "advisor": "string",
  "phone": "string",
  "email": "string",
  "address": "string",
  "status": "string",
  "gpa": "string",
  "totalCredits": number,
  "completedCredits": number,
  "failedCourses": number
}
```

### 5.3 教师模型

```json
{
  "id": "string",
  "userId": "string",
  "name": "string",
  "gender": "string",
  "birthDate": "string",
  "idCard": "string",
  "department": "string",
  "title": "string",
  "teachingYears": number,
  "phone": "string",
  "email": "string",
  "address": "string",
  "status": "string",
  "avatar": "string"
}
```

### 5.4 课程模型

```json
{
  "id": number,
  "code": "string",
  "name": "string",
  "teacherId": "string",
  "credits": number,
  "category": "string",
  "description": "string",
  "status": number,  // 1:未开始, 2:进行中, 3:已结束
  "semester": "string",
  "createTime": "string",
  "updateTime": "string"
}
```

### 5.5 成绩模型

```json
{
  "id": number,
  "studentId": "string",
  "courseId": number,
  "usualScore": number,
  "examScore": number,
  "totalScore": number,
  "gpa": number,
  "semester": "string",
  "createTime": "string",
  "updateTime": "string"
}
```

### 5.6 课程安排模型

```json
{
  "id": number,
  "courseId": number,
  "day": number,  // 1-7 表示周一到周日
  "timeSlot": number,  // 1-5 表示不同的时间段
  "location": "string",
  "classId": string,
  "semester": "string"
}
```

### 5.7 选课记录模型

```json
{
  "id": number,
  "studentId": "string",
  "courseId": number,
  "selectTime": "string",
  "status": "string",  // selected, dropped
  "semester": "string"
}
```

### 5.8 图书借阅模型

```json
{
  "id": number,
  "studentId": "string",
  "bookId": number,
  "borrowDate": "string",
  "dueDate": "string",
  "returnDate": "string",
  "status": "string"  // borrowed, returned, overdue
}
```

### 5.9 公告模型

```json
{
  "id": number,
  "title": "string",
  "content": "string",
  "department": "string",
  "type": "string",  // important, notice, info, activity
  "publisherId": "string",
  "publishTime": "string",
  "status": "string"
}
```

### 5.10 消息模型

```json
{
  "id": number,
  "senderId": "string",
  "receiverId": "string",
  "content": "string",
  "sendTime": "string",
  "readTime": "string",
  "type": "string",  // received, sent
  "status": "string"
}
```

## 6. 注意事项

1. 所有接口都需要在请求头中携带 `Authorization: Bearer {token}` 进行身份验证。
2. 分页参数 `page` 和 `size` 的默认值分别为 1 和 10。
3. 日期时间格式统一使用 ISO 8601 标准，如 `2023-11-01T12:00:00Z`。
4. 文件上传接口需要使用 `multipart/form-data` 格式。
5. 所有接口返回的数据中，敏感信息（如密码）不应包含在内。
6. 接口开发时应考虑数据安全性，对用户输入进行验证和过滤，防止 SQL 注入等安全问题。
7. 对于需要权限控制的接口，应在后端进行权限校验，确保用户只能访问自己有权限的资源。