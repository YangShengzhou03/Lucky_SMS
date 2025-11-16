import request from '@/utils/request'

// 教师功能模块API
export default {
  // 获取学生列表（带分页）
  getStudentsList(params) {
    return request({
      url: '/teacher/students',
      method: 'get',
      params
    })
  },
  
  // 获取课程列表（带分页）
  getCoursesList(params) {
    return request({
      url: '/teacher/courses',
      method: 'get',
      params
    })
  },
  
  // 获取教师个人信息
  getTeacherProfile() {
    return request({
      url: '/teacher/profile',
      method: 'get'
    })
  },
  
  // 更新教师个人信息
  updateTeacherProfile(data) {
    return request({
      url: '/teacher/profile',
      method: 'post',
      data
    })
  },
  
  // 获取教师首页数据
  getHomeData() {
    return request({
      url: '/teacher/home',
      method: 'get'
    })
  },
  
  // 获取学生成绩
  getStudentGrades(params) {
    return request({
      url: '/teacher/grades',
      method: 'get',
      params
    })
  },
  
  // 录入/更新学生成绩
  updateStudentGrade(data) {
    return request({
      url: '/teacher/grades',
      method: 'post',
      data
    })
  },
  
  // 获取课程表
  getSchedule(params) {
    return request({
      url: '/teacher/schedule',
      method: 'get',
      params
    })
  },
  
  // 获取消息列表
  getMessages(params) {
    return request({
      url: '/teacher/messages',
      method: 'get',
      params
    })
  },
  
  // 发送消息
  sendMessage(data) {
    return request({
      url: '/teacher/messages',
      method: 'post',
      data
    })
  },
  
  // 标记消息已读
  markMessageRead(messageId) {
    return request({
      url: `/teacher/messages/${messageId}/read`,
      method: 'post'
    })
  }
}