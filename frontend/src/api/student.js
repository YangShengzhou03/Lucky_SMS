import request from '@/utils/request'

export function getAvailableCourses() {
  return request({
    url: '/student/courses/available',
    method: 'get'
  })
}

export function getAvailableCoursesWithPagination(params) {
  return request({
    url: '/student/courses/available/pagination',
    method: 'get',
    params
  })
}

export function getSelectedCourses() {
  return request({
    url: '/student/courses/selected',
    method: 'get'
  })
}

export function getSelectedCoursesWithPagination(params) {
  return request({
    url: '/student/courses/selected/pagination',
    method: 'get',
    params
  })
}

export function selectCourse(courseId) {
  return request({
    url: '/student/courses/select',
    method: 'post',
    params: {
      courseId: courseId
    }
  })
}

export function dropCourse(courseId) {
  return request({
    url: '/student/courses/drop',
    method: 'post',
    data: {
      courseId: courseId
    }
  })
}