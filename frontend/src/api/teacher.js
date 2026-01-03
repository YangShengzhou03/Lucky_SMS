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
  }
}
