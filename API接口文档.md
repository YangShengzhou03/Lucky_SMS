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

### 1.1 账号密码登录

**接口地址**: `POST /login/password`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | string | 是 | 手机号 |
| password | string | 是 | 密码 |

**响应示例**:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "string",
    "uid": "number",
    "username": "string",
    "phone": "string",
    "role": "string"  // ADMIN, STUDENT, TEACHER
  }
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "手机号和密码不能为空",
  "data": null
}
```

```json
{
  "code": 401,
  "message": "手机号或密码错误",
  "data": null
}
```

### 1.2 手机号登录

**接口地址**: `POST /login/phone`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | string | 是 | 手机号 |
| captcha | string | 是 | 验证码 |

**响应示例**:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "string",
    "uid": "number",
    "username": "string",
    "phone": "string",
    "role": "string"  // ADMIN, STUDENT, TEACHER
  }
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "手机号和验证码不能为空",
  "data": null
}
```

```json
{
  "code": 401,
  "message": "用户不存在",
  "data": null
}
```

### 1.3 发送验证码

**接口地址**: `POST /captcha/send`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | string | 是 | 手机号 |

**响应示例**:

```json
{
  "code": 200,
  "message": "验证码已发送（测试环境：123456）",
  "data": null
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "手机号不能为空",
  "data": null
}
```

### 1.4 重置密码

**接口地址**: `POST /resetPassword`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | string | 是 | 手机号 |
| captcha | string | 是 | 验证码 |
| newPassword | string | 是 | 新密码 |

**响应示例**:

```json
{
  "code": 200,
  "message": "密码重置成功",
  "data": null
}
```

### 1.5 获取用户信息

**接口地址**: `GET /getUserInfo`

**请求参数**: 无（需携带JWT Token）

**响应示例**:

```json
{
  "code": 200,
  "message": "获取用户信息成功",
  "data": {
    "userId": "string",
    "username": "string",
    "role": "string",
    "date": "string"
  }
}
```

**错误响应**:

```json
{
  "code": 401,
  "message": "用户未登录",
  "data": null
}
```

### 1.6 退出登录

**接口地址**: `POST /logout`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "退出登录成功",
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
  "message": "请求成功",
  "data": {
    "student": {
      "username": "string",
      "student_no": "string",
      "class_name": "string",
      "gpa": "string",
      "class_rank": "string",
      "classSize": "string",
      "course_count": "string",
      "nextCourse": {
        "name": "string",
        "time": "string",
        "location": "string"
      },
      "todos": [
        {
          "id": number,
          "text": "string",
          "completed": boolean,
          "dueDate": "string",
          "important": boolean,
          "category": "string"
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

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
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

**错误响应**:

```json
{
  "code": 401,
  "message": "用户未登录",
  "data": null
}
```

### 2.2.1 按学期查询成绩

**接口地址**: `GET /student/grades/{semester}`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semester | string | 是 | 学期，如 "2023-2024-2" |

**响应示例**: 同2.2学生成绩查询响应格式

### 2.2.3 学生成绩查询（分页）

**接口地址**: `GET /student/grades/pagination`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**: 同2.2学生成绩查询响应格式

### 2.2.4 按学期查询成绩（分页）

**接口地址**: `GET /student/grades/{semester}/pagination`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semester | string | 是 | 学期，如 "2023-2024-2" |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**: 同2.2学生成绩查询响应格式

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

### 2.5 查询可选课程

**接口地址**: `GET /student/courses/available`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semesterId | number | 否 | 学期ID，默认 1 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": [
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
  ]
}
```

### 2.5.1 查询可选课程（分页）

**接口地址**: `GET /student/courses/available/pagination`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semesterId | number | 否 | 学期ID，默认 1 |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**: 同2.5查询可选课程响应格式

### 2.6 查询已选课程

**接口地址**: `GET /student/courses/selected`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semesterId | number | 否 | 学期ID，默认 1 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": [
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
```

### 2.6.1 查询已选课程（分页）

**接口地址**: `GET /student/courses/selected/pagination`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semesterId | number | 否 | 学期ID，默认 1 |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**: 同2.6查询已选课程响应格式

### 2.7 选课操作

**接口地址**: `POST /student/courses/select`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| assignmentId | number | 是 | 教学任务ID |
| selectionTypeId | number | 否 | 选课类型ID，默认 1 |

**响应示例**:

```json
{
  "code": 200,
  "message": "选课成功",
  "data": {
    "selectionId": number,
    "status": "string",
    "message": "string"
  }
}
```

### 2.8 退课操作

**接口地址**: `POST /student/courses/drop`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| selectionId | number | 是 | 选课记录ID |
| dropReason | string | 否 | 退课原因，默认 "个人原因" |

**响应示例**:

```json
{
  "code": 200,
  "message": "退课成功",
  "data": {
    "selectionId": number,
    "status": "string",
    "message": "string"
  }
}
```

### 2.9 获取学生个人信息

**接口地址**: `GET /student/settings`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取个人信息成功",
  "data": {
    "studentId": "string",
    "username": "string",
    "realName": "string",
    "phone": "string",
    "email": "string",
    "address": "string",
    "avatar": "string",
    "gender": "string",
    "birthday": "string",
    "className": "string",
    "major": "string",
    "department": "string"
  }
}
```

**错误响应**:

```json
{
  "code": 401,
  "message": "用户未登录",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "获取个人信息失败",
  "data": null
}
```

**接口地址**: `POST /student/setting/info`

**请求参数**:

```json
{
  "phone": "string",          // 手机号
  "email": "string",          // 邮箱
  "address": "string",        // 地址
  "emergencyContact": "string" // 紧急联系人
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "更新个人信息成功",
  "data": null
}
```

**错误响应**:

```json
{
  "code": 401,
  "message": "用户未登录",
  "data": null
}
```

```json
{
  "code": 500,
  "message": "更新个人信息失败",
  "data": null
}
```

### 2.10 图书搜索

**接口地址**: `GET /student/library/search`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| query | string | 否 | 搜索关键词 |
| type | string | 否 | 搜索类型 |
| sort | string | 否 | 排序方式，默认 "relevance" |
| location | string | 否 | 馆藏位置 |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "搜索成功",
  "data": {
    "records": [
      {
        "id": number,
        "title": "string",
        "author": "string",
        "available": boolean,
        "availableCopies": number,
        "cover": "string",
        "returnDate": "string"  // 如果不可借
      }
    ],
    "total": number,
    "size": number,
    "current": number,
    "pages": number
  }
}
```

### 2.11 图书详情

**接口地址**: `GET /student/library/detail/{bookId}`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": {
    "id": number,
    "title": "string",
    "author": "string",
    "available": boolean,
    "availableCopies": number,
    "cover": "string",
    "publisher": "string",
    "year": "string",
    "isbn": "string",
    "pages": "string",
    "callNumber": "string",
    "location": "string",
    "description": "string",
    "type": "string"
  }
}
```

### 2.12 预约图书

**接口地址**: `POST /student/library/reserve`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| bookId | number | 是 | 图书ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "预约成功，请在3天内到图书馆借阅",
  "data": null
}
```

### 2.13 我的借阅记录

**接口地址**: `GET /student/library/borrows`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": number,
      "title": "string",
      "borrowDate": "string",
      "dueDate": "string",
      "renewTimes": number,
      "cover": "string"
    }
  ]
}
```

### 2.14 续借图书

**接口地址**: `POST /student/library/renew`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| borrowId | number | 是 | 借阅记录ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "续借成功，新的应还日期为30天后",
  "data": null
}
```

### 2.15 热门推荐

**接口地址**: `GET /student/library/recommended`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "获取成功",
  "data": [
    {
      "id": number,
      "title": "string",
      "isNew": boolean,
      "cover": "string"
    }
  ]
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

### 3.2 教师课程列表

**接口地址**: `GET /teacher/courses`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |
| semester | string | 否 | 学期，默认 "2023-2024-2" |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": number,
        "courseCode": "string",
        "courseName": "string",
        "courseType": "string",
        "credits": number,
        "classTime": "string",
        "location": "string",
        "studentCount": number
      }
    ],
    "total": number,
    "size": number,
    "current": number,
    "pages": number
  }
}
```

### 3.3 查询课程学生列表

**接口地址**: `GET /teacher/course/{courseId}/students`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| courseId | number | 是 | 课程ID，路径参数 |
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "studentId": "string",
        "studentName": "string",
        "className": "string",
        "phone": "string",
        "email": "string"
      }
    ],
    "total": number,
    "size": number,
    "current": number,
    "pages": number
  }
}
```

### 3.4 录入学生成绩

**接口地址**: `POST /teacher/grades`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| studentId | string | 是 | 学生ID |
| courseId | number | 是 | 课程ID |
| usualScore | number | 是 | 平时成绩 |
| examScore | number | 是 | 考试成绩 |
| totalScore | number | 是 | 总成绩 |
| semester | string | 是 | 学期 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": null
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}
```

### 3.5 查询课程成绩列表

**接口地址**: `GET /teacher/grades`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| courseId | number | 是 | 课程ID |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": [
    {
      "studentId": "string",
      "studentName": "string",
      "attendance": "number",
      "midterm": "number",
      "final": "number",
      "total": "number",
      "letterGrade": "string"
    }
  ]
}
```

### 3.6 录入学生成绩

**接口地址**: `POST /teacher/grades`

**请求参数**:

```json
{
  "studentId": "string",      // 学生ID
  "courseId": "number",       // 课程ID
  "usualScore": "number",     // 平时成绩
  "examScore": "number",      // 考试成绩
  "totalScore": "number",     // 总成绩
  "semester": "string"        // 学期
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": null
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}

### 3.7 查询教师课表

**接口地址**: `GET /teacher/schedule`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| semester | string | 否 | 学期，如 "2023-2024-2" |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
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

### 3.8 查询教师公告

**接口地址**: `GET /teacher/announcements`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": number,
        "title": "string",
        "content": "string",
        "type": "string",
        "publishDate": "string",
        "department": "string",
        "important": boolean
      }
    ],
    "total": number,
    "size": number,
    "current": number,
    "pages": number
  }
}
```

### 3.9 查询教师任务

**接口地址**: `GET /teacher/tasks`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": number,
        "text": "string",
        "dueDate": "string",
        "completed": boolean,
        "important": boolean,
        "course": "string"
      }
    ],
    "total": number,
    "size": number,
    "current": number,
    "pages": number
  }
}
```

### 3.10 更新任务状态

**接口地址**: `PUT /teacher/task/{taskId}`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| taskId | number | 是 | 任务ID，路径参数 |
| completed | boolean | 是 | 完成状态 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": null
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}
```

### 3.11 查询教师个人信息

**接口地址**: `GET /teacher/profile`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "teacherId": "string",
    "name": "string",
    "department": "string",
    "title": "string",
    "phone": "string",
    "email": "string",
    "office": "string",
    "teachingYears": number,
    "avatar": "string",
    "courseCount": number,
    "studentCount": number,
    "avgScore": number,
    "attendanceRate": number
  }
}
```

### 3.12 更新教师个人信息

**接口地址**: `PUT /teacher/profile`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| phone | string | 否 | 手机号 |
| email | string | 否 | 邮箱 |
| office | string | 否 | 办公室 |
| title | string | 否 | 职称 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": null
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}
```

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

### 3.13 查询教师消息

**接口地址**: `GET /teacher/messages`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": number,
        "title": "string",
        "content": "string",
        "date": "string",
        "isRead": boolean
      }
    ],
    "total": number,
    "size": number,
    "current": number,
    "pages": number
  }
}
```

### 3.15 发送消息

**接口地址**: `POST /teacher/messages`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| receiverId | string | 是 | 接收者ID |
| content | string | 是 | 消息内容 |

**响应示例**:

```json
{
  "code": 200,
  "message": "消息发送成功",
  "data": null
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}
```

### 3.16 标记消息已读

**接口地址**: `GET /teacher/profile`

**请求参数**: 无

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "id": "string",
    "teacherId": "string",
    "name": "string",
    "gender": "string",
    "birthDate": "string",
    "title": "string",
    "department": "string",
    "email": "string",
    "phone": "string",
    "education": "string",
    "hireDate": "string"
  }
}
```

### 3.16 更新教师个人信息

**接口地址**: `POST /teacher/profile`

**请求参数**:

```json
{
  "phone": "string",      // 手机号
  "email": "string",      // 邮箱
  "office": "string",     // 办公室
  "title": "string"       // 职称
}
```

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": null
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}
```

### 3.17 查询教师学生列表

**接口地址**: `GET /teacher/students`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": "number",
        "studentId": "string",
        "name": "string",
        "major": "string",
        "className": "string",
        "contact": "string"
      }
    ],
    "total": "number",
    "size": "number",
    "current": "number",
    "pages": "number"
  }
}
```

### 3.18 查询教师消息列表

**接口地址**: `GET /teacher/message`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| page | number | 否 | 页码，默认1 |
| size | number | 否 | 每页数量，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "message": "请求成功",
  "data": {
    "messages": [
      {
        "id": "number",
        "title": "string",
        "content": "string",
        "date": "string",
        "isRead": "boolean"
      }
    ],
    "total": "number",
    "unreadCount": "number"
  }
}
```

**接口地址**: `POST /teacher/messages/{messageId}/read`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| messageId | number | 是 | 消息ID，路径参数 |

**响应示例**:

```json
{
  "code": 200,
  "message": "消息已标记为已读",
  "data": null
}
```

**错误响应**:

```json
{
  "code": 400,
  "message": "参数错误",
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
  "message": "请求成功",
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
  "message": "请求成功",
  "data": {
    "records": [
      {
        "id": number,
        "title": "string",
        "content": "string",
        "date": "string",
        "department": "string",
        "type": "string"  // important, notice, info, activity
      }
    ],
    "total": number,
    "size": number,
    "current": number,
    "pages": number
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
  "message": "请求成功",
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

### 5.9 待办事项模型

```json
{
  "id": number,
  "text": "string",
  "completed": boolean,
  "dueDate": "string",  // 日期格式，如 "2025-06-24"
  "important": boolean,
  "category": "string"  // 分类，如 "作业"、"报告"等
}
```

### 5.10 公告模型

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

### 5.11 消息模型

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
8. 注意：登录接口的响应数据中，用户信息字段名为 `date` 而非 `data`，这是当前实现的一个特殊情况，后续版本可能会统一为 `data`。