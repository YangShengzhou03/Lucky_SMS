package service

import (
	"lucky-sms/models"
	"lucky-sms/repository"
	"lucky-sms/vo"
)

type AdminService struct {
	adminRepo *repository.AdminRepository
}

func NewAdminService() *AdminService {
	return &AdminService{
		adminRepo: repository.NewAdminRepository(),
	}
}

func (s *AdminService) GetAdminHome() (*vo.AdminHomeVO, error) {
	return s.adminRepo.GetAdminHome()
}

func (s *AdminService) GetCourseList(keyword string, page, pageSize int) ([]vo.CourseManagementVO, int64, error) {
	return s.adminRepo.GetCourseList(keyword, page, pageSize)
}

func (s *AdminService) CreateCourse(course *models.Course) error {
	return s.adminRepo.CreateCourse(course)
}

func (s *AdminService) UpdateCourse(course *models.Course) error {
	return s.adminRepo.UpdateCourse(course)
}

func (s *AdminService) DeleteCourse(id int) error {
	return s.adminRepo.DeleteCourse(id)
}

func (s *AdminService) GetDepartmentList(keyword string, page, pageSize int) ([]vo.DepartmentManagementVO, int64, error) {
	return s.adminRepo.GetDepartmentList(keyword, page, pageSize)
}

func (s *AdminService) CreateDepartment(department *models.Department) error {
	return s.adminRepo.CreateDepartment(department)
}

func (s *AdminService) UpdateDepartment(department *models.Department) error {
	return s.adminRepo.UpdateDepartment(department)
}

func (s *AdminService) DeleteDepartment(id int) error {
	return s.adminRepo.DeleteDepartment(id)
}

func (s *AdminService) GetAnnouncementList(keyword string, page, pageSize int) ([]vo.AnnouncementManagementVO, int64, error) {
	return s.adminRepo.GetAnnouncementList(keyword, page, pageSize)
}

func (s *AdminService) CreateAnnouncement(announcement *models.Announcement) error {
	return s.adminRepo.CreateAnnouncement(announcement)
}

func (s *AdminService) UpdateAnnouncement(announcement *models.Announcement) error {
	return s.adminRepo.UpdateAnnouncement(announcement)
}

func (s *AdminService) DeleteAnnouncement(id int) error {
	return s.adminRepo.DeleteAnnouncement(id)
}

func (s *AdminService) GetGradeList(keyword string, page, pageSize int) ([]vo.GradeManagementVO, int64, error) {
	return s.adminRepo.GetGradeList(keyword, page, pageSize)
}

func (s *AdminService) CreateGrade(grade *models.CourseGrade) error {
	return s.adminRepo.CreateGrade(grade)
}

func (s *AdminService) UpdateGrade(grade *models.CourseGrade) error {
	return s.adminRepo.UpdateGrade(grade)
}

func (s *AdminService) DeleteGrade(id int) error {
	return s.adminRepo.DeleteGrade(id)
}
