import request from '@/utils/request'

export function getAdminHomeData() {
  return request({
    url: '/admin/home',
    method: 'get'
  })
}

export function getUserList(params) {
  return request({
    url: '/admin/users',
    method: 'get',
    params
  })
}

export function addUser(data) {
  return request({
    url: '/admin/users',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/admin/users',
    method: 'put',
    data
  })
}

export function deleteUser(userId) {
  return request({
    url: `/admin/users/${userId}`,
    method: 'delete'
  })
}

export function resetUserPassword(userId) {
  return request({
    url: `/admin/users/${userId}/reset-password`,
    method: 'post'
  })
}

export function getCourseList(params) {
  return request({
    url: '/admin/courses',
    method: 'get',
    params
  })
}

export function addCourse(data) {
  return request({
    url: '/admin/courses',
    method: 'post',
    data
  })
}

export function updateCourse(data) {
  return request({
    url: '/admin/courses',
    method: 'put',
    data
  })
}

export function deleteCourse(courseId) {
  return request({
    url: `/admin/courses/${courseId}`,
    method: 'delete'
  })
}

export function getCourseStudents(courseId) {
  return request({
    url: `/admin/courses/${courseId}/students`,
    method: 'get'
  })
}

export function getGradeList(params) {
  return request({
    url: '/admin/grades',
    method: 'get',
    params
  })
}

export function updateGrade(data) {
  return request({
    url: '/admin/grades',
    method: 'put',
    data
  })
}

export function deleteGrade(gradeId) {
  return request({
    url: `/admin/grades/${gradeId}`,
    method: 'delete'
  })
}

export function importGrades(data) {
  return request({
    url: '/admin/grades/import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}



export function getDepartmentList(params) {
  return request({
    url: '/admin/departments',
    method: 'get',
    params
  })
}

export function addDepartment(data) {
  return request({
    url: '/admin/departments',
    method: 'post',
    data
  })
}

export function updateDepartment(data) {
  return request({
    url: '/admin/departments',
    method: 'put',
    data
  })
}

export function deleteDepartment(departmentId) {
  return request({
    url: `/admin/departments/${departmentId}`,
    method: 'delete'
  })
}

export function getSystemSettings() {
  return request({
    url: '/admin/settings',
    method: 'get'
  })
}

export function updateSystemSettings(data) {
  return request({
    url: '/admin/settings',
    method: 'put',
    data
  })
}

export function getTeacherList() {
  return request({
    url: '/admin/teachers',
    method: 'get'
  })
}

export function batchOperateUsers(data) {
  return request({
    url: '/admin/users/batch',
    method: 'post',
    data
  })
}

export function batchOperateCourses(data) {
  return request({
    url: '/admin/courses/batch',
    method: 'post',
    data
  })
}

export function getAnnouncementList(params) {
  return request({
    url: '/admin/announcements',
    method: 'get',
    params
  })
}

export function createAnnouncement(data) {
  return request({
    url: '/admin/announcements',
    method: 'post',
    data
  })
}

export function updateAnnouncement(data) {
  return request({
    url: '/admin/announcements',
    method: 'put',
    data
  })
}

export function deleteAnnouncement(announcementId) {
  return request({
    url: `/admin/announcements/${announcementId}`,
    method: 'delete'
  })
}



