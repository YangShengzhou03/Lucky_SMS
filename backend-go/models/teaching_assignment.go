package models

type TeachingAssignment struct {
	BaseEntity
	AssignmentID int `json:"assignmentId" gorm:"column:assignment_id;primaryKey;autoIncrement"`
	TeacherID    int `json:"teacherId" gorm:"column:teacher_id;index"`
	CourseID     int `json:"courseId" gorm:"column:course_id;index"`
	SemesterID   int `json:"semesterId" gorm:"column:semester_id;index"`
	StatusID     int `json:"statusId" gorm:"column:status_id"`

	Teacher  *Teacher  `json:"teacher,omitempty" gorm:"-"`
	Course   *Course   `json:"course,omitempty" gorm:"-"`
	Semester *Semester `json:"semester,omitempty" gorm:"-"`
}

func (TeachingAssignment) TableName() string {
	return "teaching_assignments"
}
