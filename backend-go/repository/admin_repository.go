package repository

import (
	"lucky-sms/config"
	"lucky-sms/models"
	"lucky-sms/vo"
)

type AdminRepository struct {
}

func NewAdminRepository() *AdminRepository {
	return &AdminRepository{}
}

func (r *AdminRepository) GetAdminHome() (*vo.AdminHomeVO, error) {
	var home vo.AdminHomeVO

	config.DB.Table("students").Where("deleted_at IS NULL").Count(&home.TotalStudents)
	config.DB.Table("teachers").Where("deleted_at IS NULL").Count(&home.TotalTeachers)
	config.DB.Table("courses").Where("deleted_at IS NULL").Count(&home.TotalCourses)
	config.DB.Table("departments").Where("deleted_at IS NULL").Count(&home.TotalDepartments)
	config.DB.Table("majors").Where("deleted_at IS NULL").Count(&home.TotalMajors)
	config.DB.Table("classes").Where("deleted_at IS NULL").Count(&home.TotalClasses)

	return &home, nil
}

func (r *AdminRepository) GetCourseList(keyword string, page, pageSize int) ([]vo.CourseManagementVO, int64, error) {
	var courses []vo.CourseManagementVO
	var total int64

	query := config.DB.Table("courses c").
		Select(`
			c.course_id, c.course_code, c.course_name, c.course_type_id, c.credit,
			c.total_hours, c.theory_hours, c.practice_hours,
			c.department_id, c.major_id, c.is_compulsory, c.status_id
		`).
		Where("c.deleted_at IS NULL")

	if keyword != "" {
		query = query.Where("c.course_code LIKE ? OR c.course_name LIKE ?",
			"%"+keyword+"%", "%"+keyword+"%")
	}

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&courses).Error

	return courses, total, err
}

func (r *AdminRepository) CreateCourse(course *models.Course) error {
	return config.DB.Create(course).Error
}

func (r *AdminRepository) UpdateCourse(course *models.Course) error {
	return config.DB.Save(course).Error
}

func (r *AdminRepository) DeleteCourse(id int) error {
	return config.DB.Delete(&models.Course{}, id).Error
}

func (r *AdminRepository) GetDepartmentList(keyword string, page, pageSize int) ([]vo.DepartmentManagementVO, int64, error) {
	var departments []vo.DepartmentManagementVO
	var total int64

	query := config.DB.Table("departments d").
		Select("d.department_id, d.department_code, d.department_name, d.description, d.status_id").
		Where("d.deleted_at IS NULL")

	if keyword != "" {
		query = query.Where("d.department_code LIKE ? OR d.department_name LIKE ?",
			"%"+keyword+"%", "%"+keyword+"%")
	}

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&departments).Error

	return departments, total, err
}

func (r *AdminRepository) CreateDepartment(department *models.Department) error {
	return config.DB.Create(department).Error
}

func (r *AdminRepository) UpdateDepartment(department *models.Department) error {
	return config.DB.Save(department).Error
}

func (r *AdminRepository) DeleteDepartment(id int) error {
	return config.DB.Delete(&models.Department{}, id).Error
}

func (r *AdminRepository) GetAnnouncementList(keyword string, page, pageSize int) ([]vo.AnnouncementManagementVO, int64, error) {
	var announcements []vo.AnnouncementManagementVO
	var total int64

	query := config.DB.Table("announcements a").
		Select("a.announcement_id, a.title, a.content, a.announcement_type, a.publish_date, a.expiry_date, a.priority, a.status_id").
		Where("a.deleted_at IS NULL")

	if keyword != "" {
		query = query.Where("a.title LIKE ? OR a.content LIKE ?",
			"%"+keyword+"%", "%"+keyword+"%")
	}

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&announcements).Error

	return announcements, total, err
}

func (r *AdminRepository) CreateAnnouncement(announcement *models.Announcement) error {
	return config.DB.Create(announcement).Error
}

func (r *AdminRepository) UpdateAnnouncement(announcement *models.Announcement) error {
	return config.DB.Save(announcement).Error
}

func (r *AdminRepository) DeleteAnnouncement(id int) error {
	return config.DB.Delete(&models.Announcement{}, id).Error
}

func (r *AdminRepository) GetGradeList(keyword string, page, pageSize int) ([]vo.GradeManagementVO, int64, error) {
	var grades []vo.GradeManagementVO
	var total int64

	query := config.DB.Table("course_grades cg").
		Select(`
			cg.grade_id, cg.student_id, s.student_no, u.real_name,
			cg.course_id, c.course_code, c.course_name,
			cg.semester_id, sm.semester_name,
			cg.regular_score, cg.final_score, cg.total_score,
			cg.grade_point, cg.grade_level, cg.graded_at
		`).
		Joins("INNER JOIN students s ON cg.student_id = s.student_id").
		Joins("INNER JOIN users u ON s.user_id = u.user_id").
		Joins("INNER JOIN courses c ON cg.course_id = c.course_id").
		Joins("INNER JOIN semesters sm ON cg.semester_id = sm.semester_id").
		Where("cg.deleted_at IS NULL")

	if keyword != "" {
		query = query.Where("s.student_no LIKE ? OR u.real_name LIKE ? OR c.course_name LIKE ?",
			"%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%")
	}

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&grades).Error

	return grades, total, err
}

func (r *AdminRepository) CreateGrade(grade *models.CourseGrade) error {
	return config.DB.Create(grade).Error
}

func (r *AdminRepository) UpdateGrade(grade *models.CourseGrade) error {
	return config.DB.Save(grade).Error
}

func (r *AdminRepository) DeleteGrade(id int) error {
	return config.DB.Delete(&models.CourseGrade{}, id).Error
}
