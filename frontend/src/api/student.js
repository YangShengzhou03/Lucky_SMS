import request from '@/utils/request'

// 获取可选课程列表
export function getAvailableCourses() {
  return request({
    url: '/student/courses/available',
    method: 'get'
  })
}

// 分页获取可选课程列表
export function getAvailableCoursesWithPagination(params) {
  return request({
    url: '/student/courses/available/pagination',
    method: 'get',
    params
  })
}

// 获取已选课程列表
export function getSelectedCourses() {
  return request({
    url: '/student/courses/selected',
    method: 'get'
  })
}

// 分页获取已选课程列表
export function getSelectedCoursesWithPagination(params) {
  return request({
    url: '/student/courses/selected/pagination',
    method: 'get',
    params
  })
}

// 选课
export function selectCourse(courseId) {
  console.log(courseId)
  return request({
    url: '/student/courses/select',
    method: 'post',
    params: {
      courseId: courseId
    }
  })
}

// 退课
export function dropCourse(courseId) {
  return request({
    url: '/student/courses/drop',
    method: 'post',
    data: {
      courseId: courseId
    }
  })
}