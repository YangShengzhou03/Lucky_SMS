import { createRouter, createWebHistory } from 'vue-router';
import IndexLayout from '@/components/IndexLayout.vue';
import StudentLayout from '@/components/StudentLayout.vue';
import LoginPage from '@/views/LoginPage.vue';
import StudentHome from '@/views/Student/HomePage.vue';
import StudentStatus from '@/views/Student/StatusPage.vue';
import StudentGrades from '@/views/Student/GradesPage.vue';
import StudentSchedule from '@/views/Student/SchedulePage.vue';
import StudentCourse from '@/views/Student/CoursePage.vue';
import TeacherLayout from '@/components/TeacherLayout.vue';
import TeacherHome from '@/views/Teacher/HomePage.vue';
import TeacherCourses from '@/views/Teacher/CoursesPage.vue';
import TeacherGrades from '@/views/Teacher/GradesPage.vue';
import TeacherStudents from '@/views/Teacher/StudentsPage.vue';
import TeacherSchedule from '@/views/Teacher/SchedulePage.vue';
import AdminLayout from '@/components/AdminLayout.vue';
import AdminHome from '@/views/Admin/HomePage.vue';
import UserManagement from '@/views/Admin/UserManagement.vue';
import CourseManagement from '@/views/Admin/CourseManagement.vue';
import GradeManagement from '@/views/Admin/GradeManagement.vue';
import DepartmentManagement from '@/views/Admin/DepartmentManagement.vue';
import AnnouncementManagement from '@/views/Admin/AnnouncementManagement.vue';

const routes = [
  {
    path: '/',
    component: IndexLayout,
    children: [
      { path: '', name: 'Home', meta: { title: 'Lucky-SMS' } },
      { path: '/login', name: 'Login', component: LoginPage, meta: { title: '登录' } }
    ]
  },

  {
    path: '/student',
    component: StudentLayout,
    children: [
      { path: '', name: 'StudentHome', component: StudentHome, meta: { title: '学生首页' } },
      { path: 'status', name: 'StudentStatus', component: StudentStatus, meta: { title: '学籍信息' } },
      { path: 'grades', name: 'StudentGrades', component: StudentGrades, meta: { title: '成绩查询' } },
      { path: 'schedule', name: 'StudentSchedule', component: StudentSchedule, meta: { title: '课表查询' } },
      { path: 'course', name: 'StudentCourse', component: StudentCourse, meta: { title: '选课系统' } }
    ]
  },

  {
    path: '/teacher',
    component: TeacherLayout,
    children: [
      { path: '', name: 'TeacherHome', component: TeacherHome, meta: { title: '教师首页' } },
      { path: 'courses', name: 'TeacherCourses', component: TeacherCourses, meta: { title: '课程管理' } },
      { path: 'grades', name: 'TeacherGrades', component: TeacherGrades, meta: { title: '成绩管理' } },
      { path: 'students', name: 'TeacherStudents', component: TeacherStudents, meta: { title: '学生管理' } },
      { path: 'schedule', name: 'TeacherSchedule', component: TeacherSchedule, meta: { title: '教学计划' } }
    ]
  },

  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: '', name: 'AdminHome', component: AdminHome, meta: { title: '管理员首页' } },
      { path: 'users', name: 'UserManagement', component: UserManagement, meta: { title: '用户管理' } },
      { path: 'courses', name: 'CourseManagement', component: CourseManagement, meta: { title: '课程管理' } },
      { path: 'grades', name: 'GradeManagement', component: GradeManagement, meta: { title: '成绩管理' } },
      { path: 'departments', name: 'DepartmentManagement', component: DepartmentManagement, meta: { title: '部门管理' } },
      { path: 'announcements', name: 'AnnouncementManagement', component: AnnouncementManagement, meta: { title: '公告管理' } }
    ]
  },

  { path: '/:pathMatch(.*)*', redirect: '/' }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to) => {
  document.title = to.meta.title || 'Lucky SMS';
});

export default router;