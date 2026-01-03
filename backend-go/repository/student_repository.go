package repository

import (
	"lucky-sms/config"
	"lucky-sms/models"
	"lucky-sms/vo"
)

type StudentRepository struct {
}

func NewStudentRepository() *StudentRepository {
	return &StudentRepository{}
}

func (r *StudentRepository) GetByUserID(userID int) (*models.Student, error) {
	var student models.Student
	err := config.DB.Where("user_id = ? AND deleted_at IS NULL", userID).First(&student).Error
	if err != nil {
		return nil, err
	}
	return &student, nil
}

func (r *StudentRepository) GetByID(studentID int) (*models.Student, error) {
	var student models.Student
	err := config.DB.Where("student_id = ? AND deleted_at IS NULL", studentID).First(&student).Error
	if err != nil {
		return nil, err
	}
	return &student, nil
}

func (r *StudentRepository) GetStudentProfile(userID int) (*vo.StudentProfileVO, error) {
	var profile vo.StudentProfileVO
	err := config.DB.Table("students s").
		Select(`
			s.student_id, s.student_no, s.user_id, s.department_id, s.major_id, s.class_id,
			s.enrollment_date, s.graduation_date, s.education_years, s.education_level_id, s.status_id,
			s.emergency_contact, s.emergency_phone, s.parent_name, s.parent_phone,
			s.home_address, s.high_school, s.entrance_score, s.scholarship_info,
			u.real_name, u.gender_id, u.birthday, u.phone, u.email, u.address
		`).
		Joins("INNER JOIN users u ON s.user_id = u.user_id").
		Where("s.user_id = ? AND s.deleted_at IS NULL", userID).
		First(&profile).Error
	return &profile, err
}

func (r *StudentRepository) UpdateStudentProfile(userID int, updates map[string]interface{}) error {
	return config.DB.Table("students s").
		Joins("INNER JOIN users u ON s.user_id = u.user_id").
		Where("s.user_id = ? AND s.deleted_at IS NULL", userID).
		Updates(updates).Error
}

func (r *StudentRepository) GetStudentHome(userID int) (*vo.HomeVO, error) {
	var home vo.HomeVO
	err := config.DB.Table("students s").
		Select(`
			u.real_name as student_name,
			s.student_no,
			c.class_name,
			m.major_name,
			d.department_name
		`).
		Joins("INNER JOIN users u ON s.user_id = u.user_id").
		Joins("LEFT JOIN classes c ON s.class_id = c.class_id").
		Joins("LEFT JOIN majors m ON s.major_id = m.major_id").
		Joins("LEFT JOIN departments d ON s.department_id = d.department_id").
		Where("s.user_id = ? AND s.deleted_at IS NULL", userID).
		First(&home).Error
	return &home, err
}

func (r *StudentRepository) GetStudentGrades(userID int, page, pageSize int) ([]vo.GradesVO, int64, error) {
	var grades []vo.GradesVO
	var total int64

	query := config.DB.Table("course_grades cg").
		Select(`
			cg.grade_id, cg.course_id, c.course_code, c.course_name, c.credit,
			cg.semester_id, s.semester_name,
			cg.regular_score, cg.final_score, cg.total_score,
			cg.grade_point, cg.grade_level
		`).
		Joins("INNER JOIN students st ON cg.student_id = st.student_id").
		Joins("INNER JOIN courses c ON cg.course_id = c.course_id").
		Joins("INNER JOIN semesters s ON cg.semester_id = s.semester_id").
		Where("st.user_id = ? AND cg.deleted_at IS NULL", userID)

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&grades).Error

	return grades, total, err
}

func (r *StudentRepository) GetAvailableCourses(userID int, semesterID int, page, pageSize int) ([]vo.CourseSelectionVO, int64, error) {
	var courses []vo.CourseSelectionVO
	var total int64

	query := config.DB.Table("courses c").
		Select(`
			c.course_id, c.course_code, c.course_name, c.credit, c.total_hours,
			t.real_name as teacher_name, 0 as selected_count, 100 as max_capacity,
			CASE WHEN cs.selection_id IS NOT NULL THEN true ELSE false END as is_selected
		`).
		Joins("LEFT JOIN teaching_assignments ta ON c.course_id = ta.course_id AND ta.semester_id = ?", semesterID).
		Joins("LEFT JOIN teachers t ON ta.teacher_id = t.teacher_id").
		Joins("LEFT JOIN course_selections cs ON c.course_id = cs.course_id AND cs.student_id IN (SELECT student_id FROM students WHERE user_id = ?)", userID).
		Where("c.status_id = 1 AND c.deleted_at IS NULL", userID)

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&courses).Error

	return courses, total, err
}

func (r *StudentRepository) SelectCourse(studentID, courseID, semesterID int) error {
	selection := &models.CourseSelection{
		StudentID:       studentID,
		CourseID:        courseID,
		SemesterID:      semesterID,
		SelectionTypeID: 1,
		StatusID:        1,
	}
	return config.DB.Create(selection).Error
}

func (r *StudentRepository) DropCourse(studentID, courseID int) error {
	return config.DB.Where("student_id = ? AND course_id = ?", studentID, courseID).
		Delete(&models.CourseSelection{}).Error
}

func (r *StudentRepository) GetCourseSelectionStatistics(userID int) (*vo.CourseSelectionStatisticsVO, error) {
	var stats vo.CourseSelectionStatisticsVO

	config.DB.Table("course_selections cs").
		Select("COUNT(*) as selected_courses").
		Joins("INNER JOIN students s ON cs.student_id = s.student_id").
		Where("s.user_id = ? AND cs.deleted_at IS NULL", userID).
		Scan(&stats.SelectedCourses)

	config.DB.Table("course_selections cs").
		Select("SUM(c.credit) as total_credits").
		Joins("INNER JOIN students s ON cs.student_id = s.student_id").
		Joins("INNER JOIN courses c ON cs.course_id = c.course_id").
		Where("s.user_id = ? AND cs.deleted_at IS NULL", userID).
		Scan(&stats.TotalCredits)

	return &stats, nil
}

func (r *StudentRepository) GetStudentStatus(userID int) (*vo.StatusVO, error) {
	var status vo.StatusVO
	err := config.DB.Table("students s").
		Select("st.status_id, st.status_name").
		Joins("INNER JOIN status_types st ON s.status_id = st.status_id").
		Where("s.user_id = ? AND s.deleted_at IS NULL", userID).
		First(&status).Error
	return &status, err
}

func (r *StudentRepository) Create(student *models.Student) error {
	return config.DB.Create(student).Error
}

func (r *StudentRepository) Update(student *models.Student) error {
	return config.DB.Save(student).Error
}

func (r *StudentRepository) Delete(id int) error {
	return config.DB.Delete(&models.Student{}, id).Error
}
