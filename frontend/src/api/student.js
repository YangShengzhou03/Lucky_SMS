import request from '@/utils/request'

export function getAvailableCoursesWithPagination(params) {
  return request({
    url: '/student/courses/available/pagination',
    method: 'get',
    params
  })
}

export function getSelectedCoursesWithPagination(params) {
  return request({
    url: '/student/courses/selected/pagination',
    method: 'get',
    params
  })
}

export function selectCourse(assignmentId) {
  return request({
    url: '/student/courses/select',
    method: 'post',
    params: {
      assignmentId: assignmentId
    }
  })
}

export function dropCourse(selectionId) {
  return request({
    url: '/student/courses/drop',
    method: 'post',
    params: {
      selectionId: selectionId
    }
  })
}

export function getHomeData() {
  return request({
    url: '/student/home',
    method: 'get'
  })
}

export function getGradesWithPagination(params) {
  return request({
    url: '/student/grades/pagination',
    method: 'get',
    params
  })
}

export function getGradesBySemesterWithPagination(semester, params) {
  return request({
    url: `/student/grades/${semester}/pagination`,
    method: 'get',
    params
  })
}

export function getStudentProfile() {
  return request({
    url: '/student/profile',
    method: 'get'
  })
}

export function updateStudentProfile(data) {
  return request({
    url: '/student/profile',
    method: 'post',
    data
  })
}

export function getStudentSchedule(params) {
  return request({
    url: '/student/schedule',
    method: 'get',
    params
  })
}
