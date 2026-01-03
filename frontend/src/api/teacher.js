import request from '@/utils/request'

export default {
  getStudentsList(params) {
    return request({
      url: '/teacher/students',
      method: 'get',
      params
    })
  },
  
  getCoursesList(params) {
    return request({
      url: '/teacher/courses',
      method: 'get',
      params
    })
  },
  
  getTeacherProfile() {
    return request({
      url: '/teacher/profile',
      method: 'get'
    })
  },
  
  updateTeacherProfile(data) {
    return request({
      url: '/teacher/profile',
      method: 'post',
      data
    })
  },
  
  getHomeData() {
    return request({
      url: '/teacher/home',
      method: 'get'
    })
  },
  
  getStudentGrades(params) {
    return request({
      url: '/teacher/grades',
      method: 'get',
      params
    })
  },
  
  updateStudentGrade(data) {
    return request({
      url: '/teacher/grades',
      method: 'post',
      data
    })
  },
  
  getSchedule(params) {
    return request({
      url: '/teacher/schedule',
      method: 'get',
      params
    })
  }
}