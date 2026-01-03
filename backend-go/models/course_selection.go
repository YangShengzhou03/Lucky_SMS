package models

import "time"

type CourseSelection struct {
	BaseEntity
	SelectionID     int       `json:"selectionId" gorm:"column:selection_id;primaryKey;autoIncrement"`
	StudentID       int       `json:"studentId" gorm:"column:student_id;index"`
	CourseID        int       `json:"courseId" gorm:"column:course_id;index"`
	SemesterID      int       `json:"semesterId" gorm:"column:semester_id;index"`
	SelectionTypeID int       `json:"selectionTypeId" gorm:"column:selection_type_id"`
	StatusID        int       `json:"statusId" gorm:"column:status_id"`
	SelectionDate   *time.Time `json:"selectionDate" gorm:"column:selection_date"`
	
	Student  *Student  `json:"student,omitempty" gorm:"-"`
	Course   *Course   `json:"course,omitempty" gorm:"-"`
	Semester *Semester `json:"semester,omitempty" gorm:"-"`
}

func (CourseSelection) TableName() string {
	return "course_selections"
}
