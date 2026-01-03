package models

import "time"

type CourseGrade struct {
	BaseEntity
	GradeID        int        `json:"gradeId" gorm:"column:grade_id;primaryKey;autoIncrement"`
	StudentID      int        `json:"studentId" gorm:"column:student_id;index"`
	CourseID       int        `json:"courseId" gorm:"column:course_id;index"`
	SemesterID     int        `json:"semesterId" gorm:"column:semester_id;index"`
	RegularScore   float64    `json:"regularScore" gorm:"column:regular_score;type:decimal(5,2)"`
	FinalScore     float64    `json:"finalScore" gorm:"column:final_score;type:decimal(5,2)"`
	TotalScore     float64    `json:"totalScore" gorm:"column:total_score;type:decimal(5,2)"`
	GradePoint     float64    `json:"gradePoint" gorm:"column:grade_point;type:decimal(3,2)"`
	GradeLevel     string     `json:"gradeLevel" gorm:"column:grade_level;size:10"`
	StatusID       int        `json:"statusId" gorm:"column:status_id"`
	GradedAt       *time.Time `json:"gradedAt" gorm:"column:graded_at"`
	GradedBy       int        `json:"gradedBy" gorm:"column:graded_by"`
	
	Student  *Student  `json:"student,omitempty" gorm:"-"`
	Course   *Course   `json:"course,omitempty" gorm:"-"`
	Semester *Semester `json:"semester,omitempty" gorm:"-"`
}

func (CourseGrade) TableName() string {
	return "course_grades"
}
