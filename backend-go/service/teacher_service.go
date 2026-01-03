package service

import (
	"lucky-sms/repository"
	"lucky-sms/vo"
)

type TeacherService struct {
	teacherRepo *repository.TeacherRepository
}

func NewTeacherService() *TeacherService {
	return &TeacherService{
		teacherRepo: repository.NewTeacherRepository(),
	}
}

func (s *TeacherService) GetTeacherProfile(userID int) (*vo.TeacherProfileVO, error) {
	return s.teacherRepo.GetTeacherProfile(userID)
}

func (s *TeacherService) GetTeacherHome(userID int) (*vo.HomeVO, error) {
	return s.teacherRepo.GetTeacherHome(userID)
}

func (s *TeacherService) GetTeacherCourses(teacherID, semesterID, page, pageSize int) ([]vo.CourseManagementVO, int64, error) {
	return s.teacherRepo.GetTeacherCourses(teacherID, semesterID, page, pageSize)
}

func (s *TeacherService) GetCourseStudents(courseID, semesterID, page, pageSize int) ([]vo.StudentManagementVO, int64, error) {
	return s.teacherRepo.GetCourseStudents(courseID, semesterID, page, pageSize)
}

func (s *TeacherService) GetCourseGrades(courseID, semesterID, page, pageSize int) ([]vo.GradeManagementVO, int64, error) {
	return s.teacherRepo.GetCourseGrades(courseID, semesterID, page, pageSize)
}

func (s *TeacherService) UpdateGrade(gradeID int, regularScore, finalScore, totalScore, gradePoint float64, gradeLevel string, gradedBy int) error {
	return s.teacherRepo.UpdateGrade(gradeID, regularScore, finalScore, totalScore, gradePoint, gradeLevel, gradedBy)
}

func (s *TeacherService) GetTeacherSchedule(teacherID, semesterID, page, pageSize int) ([]vo.ScheduleVO, int64, error) {
	return s.teacherRepo.GetTeacherSchedule(teacherID, semesterID, page, pageSize)
}
