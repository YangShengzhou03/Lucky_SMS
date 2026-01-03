# Lucky SMS - 学生管理系统

## 项目简介
这是一个基于Spring Boot + Vue.js的学生管理系统，包含管理员、教师和学生三个角色，实现了课程管理、选课、成绩管理等功能。

## 技术栈
- **后端**: Spring Boot 3.4.5 + MyBatis Plus 3.5.6 + MySQL 8.0
- **前端**: Vue.js 3 + Element Plus
- **认证**: JWT (jjwt 0.12.6)

## 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

## 数据库配置

### 1. 创建数据库
```bash
mysql -u root -p < init.sql
```

### 2. 修改数据库配置
编辑 `backend-java/src/main/resources/application-dev.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/lucky_sms?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

## 后端启动

### 1. 进入后端目录
```bash
cd backend-java
```

### 2. 使用Maven编译运行
```bash
mvn clean install
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080` 启动

## 前端启动

### 1. 进入前端目录
```bash
cd frontend
```

### 2. 安装依赖
```bash
npm install
```

### 3. 启动开发服务器
```bash
npm run serve
```

前端服务将在 `http://localhost:8081` 启动

## 默认账号

### 管理员
- 用户名: `admin`
- 密码: `123456`

### 教师
- 用户名: `teacher1`
- 密码: `123456`

### 学生
- 用户名: `student1`
- 密码: `123456`

## 功能模块

### 管理员功能
- 用户管理（增删改查）
- 课程管理（增删改查）
- 成绩管理（增删改查）
- 部门管理（增删改查）
- 公告管理（增删改查）
- 专业管理（增删改查）
- 班级管理（增删改查）
- 学期管理（增删改查）
- 系统设置

### 教师功能
- 课程管理
- 成绩录入
- 学生管理
- 课表查询

### 学生功能
- 选课/退课
- 成绩查询
- 课表查询
- 个人信息管理

## API接口说明

### 管理员接口
- `GET /admin/users` - 获取用户列表
- `POST /admin/users` - 添加用户
- `PUT /admin/users` - 更新用户
- `DELETE /admin/users/{userId}` - 删除用户
- `GET /admin/courses` - 获取课程列表
- `POST /admin/courses` - 添加课程
- `PUT /admin/courses` - 更新课程
- `DELETE /admin/courses/{courseId}` - 删除课程
- `GET /admin/grades` - 获取成绩列表
- `PUT /admin/grades` - 更新成绩
- `DELETE /admin/grades/{gradeId}` - 删除成绩
- `GET /admin/departments` - 获取部门列表
- `POST /admin/departments` - 添加部门
- `PUT /admin/departments` - 更新部门
- `DELETE /admin/departments/{deptId}` - 删除部门
- `GET /admin/announcements` - 获取公告列表
- `POST /admin/announcements` - 创建公告
- `PUT /admin/announcements` - 更新公告
- `DELETE /admin/announcements/{announcementId}` - 删除公告
- `GET /admin/teachers` - 获取教师列表

### 学生接口
- `GET /student/home` - 获取首页数据
- `GET /student/grades` - 获取成绩
- `GET /student/grades/{semester}` - 按学期获取成绩
- `GET /student/grades/pagination` - 分页获取成绩
- `GET /student/grades/{semester}/pagination` - 按学期分页获取成绩
- `GET /student/courses/available/pagination` - 分页获取可选课程
- `GET /student/courses/selected/pagination` - 分页获取已选课程
- `POST /student/courses/select` - 选课
- `POST /student/courses/drop` - 退课
- `GET /student/profile` - 获取个人信息
- `POST /student/profile` - 更新个人信息
- `GET /student/schedule` - 获取课表

### 教师接口
- `GET /teacher/students` - 获取学生列表
- `GET /teacher/courses` - 获取课程列表

## 注意事项

1. 确保MySQL服务已启动
2. 确保数据库配置正确
3. 前后端端口不要冲突
4. 首次运行需要执行init.sql初始化数据库
5. 默认密码为BCrypt加密后的`123456`

## 项目结构

```
Lucky_SMS/
├── backend-java/          # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/      # Java源码
│   │   │   └── resources/ # 配置文件
│   └── pom.xml           # Maven配置
├── frontend/             # 前端项目
│   ├── src/
│   │   ├── api/          # API接口
│   │   ├── components/   # 组件
│   │   ├── views/        # 页面
│   │   └── router/       # 路由
│   └── package.json      # npm配置
└── init.sql             # 数据库初始化脚本
```

## 常见问题

### 1. 后端启动失败
- 检查JDK版本是否为17+
- 检查MySQL连接配置是否正确
- 检查端口8080是否被占用

### 2. 前端启动失败
- 检查Node.js版本是否为16+
- 删除node_modules重新安装依赖
- 检查端口8081是否被占用

### 3. 登录失败
- 检查数据库是否初始化
- 检查用户名密码是否正确
- 查看后端日志排查错误

## 开发说明

本项目为本科毕业设计，代码结构清晰，功能完整。如需二次开发，建议：

1. 先熟悉数据库表结构
2. 了解前后端接口对应关系
3. 参考现有功能模块进行扩展

## 许可证
MIT License
