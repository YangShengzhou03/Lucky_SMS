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

export function getHomeData() {
  return request({
    url: '/student/home',
    method: 'get'
  })
}

export function getGrades() {
  return request({
    url: '/student/grades',
    method: 'get'
  })
}

export function getGradesBySemester(semester) {
  return request({
    url: `/student/grades/${semester}`,
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

export function getStudentStatus() {
  return request({
    url: '/student/status',
    method: 'get'
  })
}

export function getStudentSchedule(params) {
  return request({
    url: '/student/schedule',
    method: 'get',
    params
  })
}

export function getStudentAnnouncements(params) {
  return request({
    url: '/student/announcements',
    method: 'get',
    params
  })
}

export function markAnnouncementRead(announcementId) {
  return request({
    url: `/student/announcements/${announcementId}/read`,
    method: 'post'
  })
}

export function getStudentSettings() {
  return request({
    url: '/student/settings',
    method: 'get'
  })
}

export function updateStudentSettings(data) {
  return request({
    url: '/student/settings',
    method: 'post',
    data
  })
}