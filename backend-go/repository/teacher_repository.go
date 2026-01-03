package repository

import (
	"lucky-sms/config"
	"lucky-sms/models"
	"lucky-sms/vo"
)

type TeacherRepository struct {
}

func NewTeacherRepository() *TeacherRepository {
	return &TeacherRepository{}
}

func (r *TeacherRepository) GetByUserID(userID int) (*models.Teacher, error) {
	var teacher models.Teacher
	err := config.DB.Where("user_id = ? AND deleted_at IS NULL", userID).First(&teacher).Error
	if err != nil {
		return nil, err
	}
	return &teacher, nil
}

func (r *TeacherRepository) GetByID(teacherID int) (*models.Teacher, error) {
	var teacher models.Teacher
	err := config.DB.Where("teacher_id = ? AND deleted_at IS NULL", teacherID).First(&teacher).Error
	if err != nil {
		return nil, err
	}
	return &teacher, nil
}

func (r *TeacherRepository) GetTeacherProfile(userID int) (*vo.TeacherProfileVO, error) {
	var profile vo.TeacherProfileVO
	err := config.DB.Table("teachers t").
		Select(`
			t.teacher_id, t.teacher_no, t.user_id, t.department_id, t.title_id,
			t.hire_date, t.office_location, t.phone, t.email,
			t.research_direction, t.education_background, t.work_experience,
			u.real_name, u.gender_id, u.birthday, u.address
		`).
		Joins("INNER JOIN users u ON t.user_id = u.user_id").
		Where("t.user_id = ? AND t.deleted_at IS NULL", userID).
		First(&profile).Error
	return &profile, err
}

func (r *TeacherRepository) GetTeacherHome(userID int) (*vo.HomeVO, error) {
	var home vo.HomeVO
	err := config.DB.Table("teachers t").
		Select(`
			u.real_name as teacher_name,
			t.teacher_no,
			ti.title_name,
			d.department_name
		`).
		Joins("INNER JOIN users u ON t.user_id = u.user_id").
		Joins("LEFT JOIN titles ti ON t.title_id = ti.title_id").
		Joins("LEFT JOIN departments d ON t.department_id = d.department_id").
		Where("t.user_id = ? AND t.deleted_at IS NULL", userID).
		First(&home).Error
	return &home, err
}

func (r *TeacherRepository) GetTeacherCourses(teacherID, semesterID int, page, pageSize int) ([]vo.CourseManagementVO, int64, error) {
	var courses []vo.CourseManagementVO
	var total int64

	query := config.DB.Table("teaching_assignments ta").
		Select(`
			c.course_id, c.course_code, c.course_name, c.credit, c.total_hours,
			c.theory_hours, c.practice_hours,
			COUNT(cs.selection_id) as selected_count,
			ta.semester_id, s.semester_name
		`).
		Joins("INNER JOIN courses c ON ta.course_id = c.course_id").
		Joins("INNER JOIN semesters s ON ta.semester_id = s.semester_id").
		Joins("LEFT JOIN course_selections cs ON c.course_id = cs.course_id").
		Where("ta.teacher_id = ? AND ta.semester_id = ?", teacherID, semesterID).
		Group("c.course_id, c.course_code, c.course_name, c.credit, c.total_hours, c.theory_hours, c.practice_hours, ta.semester_id, s.semester_name")

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&courses).Error

	return courses, total, err
}

func (r *TeacherRepository) GetCourseStudents(courseID, semesterID int, page, pageSize int) ([]vo.StudentManagementVO, int64, error) {
	var students []vo.StudentManagementVO
	var total int64

	query := config.DB.Table("course_selections cs").
		Select(`
			s.student_id, s.student_no, u.real_name,
			s.class_id, c.class_name,
			s.major_id, m.major_name,
			s.department_id, d.department_name
		`).
		Joins("INNER JOIN students s ON cs.student_id = s.student_id").
		Joins("INNER JOIN users u ON s.user_id = u.user_id").
		Joins("LEFT JOIN classes c ON s.class_id = c.class_id").
		Joins("LEFT JOIN majors m ON s.major_id = m.major_id").
		Joins("LEFT JOIN departments d ON s.department_id = d.department_id").
		Where("cs.course_id = ? AND cs.semester_id = ?", courseID, semesterID)

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&students).Error

	return students, total, err
}

func (r *TeacherRepository) GetCourseGrades(courseID, semesterID int, page, pageSize int) ([]vo.GradeManagementVO, int64, error) {
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
		Where("cg.course_id = ? AND cg.semester_id = ?", courseID, semesterID)

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&grades).Error

	return grades, total, err
}

func (r *TeacherRepository) UpdateGrade(gradeID int, regularScore, finalScore, totalScore, gradePoint float64, gradeLevel string, gradedBy int) error {
	return config.DB.Model(&models.CourseGrade{}).
		Where("grade_id = ?", gradeID).
		Updates(map[string]interface{}{
			"regular_score": regularScore,
			"final_score":   finalScore,
			"total_score":   totalScore,
			"grade_point":   gradePoint,
			"grade_level":   gradeLevel,
			"graded_by":     gradedBy,
		}).Error
}

func (r *TeacherRepository) GetTeacherSchedule(teacherID, semesterID int, page, pageSize int) ([]vo.ScheduleVO, int64, error) {
	var schedules []vo.ScheduleVO
	var total int64

	query := config.DB.Table("teaching_assignments ta").
		Select(`
			c.course_id, c.course_code, c.course_name,
			ta.semester_id, s.semester_name,
			COUNT(cs.selection_id) as selected_count
		`).
		Joins("INNER JOIN courses c ON ta.course_id = c.course_id").
		Joins("INNER JOIN semesters s ON ta.semester_id = s.semester_id").
		Joins("LEFT JOIN course_selections cs ON c.course_id = cs.course_id").
		Where("ta.teacher_id = ? AND ta.semester_id = ?", teacherID, semesterID).
		Group("c.course_id, c.course_code, c.course_name, ta.semester_id, s.semester_name")

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&schedules).Error

	return schedules, total, err
}

func (r *TeacherRepository) Create(teacher *models.Teacher) error {
	return config.DB.Create(teacher).Error
}

func (r *TeacherRepository) Update(teacher *models.Teacher) error {
	return config.DB.Save(teacher).Error
}

func (r *TeacherRepository) Delete(id int) error {
	return config.DB.Delete(&models.Teacher{}, id).Error
}
