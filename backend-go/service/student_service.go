package service

import (
	"lucky-sms/repository"
	"lucky-sms/vo"
)

type StudentService struct {
	studentRepo *repository.StudentRepository
}

func NewStudentService() *StudentService {
	return &StudentService{
		studentRepo: repository.NewStudentRepository(),
	}
}

func (s *StudentService) GetStudentProfile(userID int) (*vo.StudentProfileVO, error) {
	return s.studentRepo.GetStudentProfile(userID)
}

func (s *StudentService) UpdateStudentProfile(userID int, updates map[string]interface{}) error {
	return s.studentRepo.UpdateStudentProfile(userID, updates)
}

func (s *StudentService) GetStudentHome(userID int) (*vo.HomeVO, error) {
	return s.studentRepo.GetStudentHome(userID)
}

func (s *StudentService) GetStudentGrades(userID int, page, pageSize int) ([]vo.GradesVO, int64, error) {
	return s.studentRepo.GetStudentGrades(userID, page, pageSize)
}

func (s *StudentService) GetAvailableCourses(userID, semesterID, page, pageSize int) ([]vo.CourseSelectionVO, int64, error) {
	return s.studentRepo.GetAvailableCourses(userID, semesterID, page, pageSize)
}

func (s *StudentService) SelectCourse(studentID, courseID, semesterID int) (*vo.CourseSelectionResultVO, error) {
	err := s.studentRepo.SelectCourse(studentID, courseID, semesterID)
	if err != nil {
		return &vo.CourseSelectionResultVO{Success: false, Message: "选课失败"}, err
	}
	return &vo.CourseSelectionResultVO{Success: true, Message: "选课成功"}, nil
}

func (s *StudentService) DropCourse(studentID, courseID int) (*vo.CourseSelectionResultVO, error) {
	err := s.studentRepo.DropCourse(studentID, courseID)
	if err != nil {
		return &vo.CourseSelectionResultVO{Success: false, Message: "退课失败"}, err
	}
	return &vo.CourseSelectionResultVO{Success: true, Message: "退课成功"}, nil
}

func (s *StudentService) GetCourseSelectionStatistics(userID int) (*vo.CourseSelectionStatisticsVO, error) {
	return s.studentRepo.GetCourseSelectionStatistics(userID)
}

func (s *StudentService) GetStudentStatus(userID int) (*vo.StatusVO, error) {
	return s.studentRepo.GetStudentStatus(userID)
}
