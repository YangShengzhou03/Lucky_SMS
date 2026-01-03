package models

import (
	"math/big"
)

type Course struct {
	BaseEntity
	CourseID      int         `json:"courseId" gorm:"column:course_id;primaryKey;autoIncrement"`
	CourseCode    string      `json:"courseCode" gorm:"column:course_code;size:20;uniqueIndex"`
	CourseName    string      `json:"courseName" gorm:"column:course_name;size:100"`
	CourseTypeID  int         `json:"courseTypeId" gorm:"column:course_type_id;index"`
	Credit        *big.Float  `json:"credit" gorm:"column:credit;type:decimal(3,1)"`
	TotalHours    int         `json:"totalHours" gorm:"column:total_hours"`
	TheoryHours   int         `json:"theoryHours" gorm:"column:theory_hours"`
	PracticeHours int         `json:"practiceHours" gorm:"column:practice_hours"`
	DepartmentID  int         `json:"departmentId" gorm:"column:department_id;index"`
	MajorID       int         `json:"majorId" gorm:"column:major_id;index"`
	Description   string      `json:"description" gorm:"column:description;type:text"`
	Syllabus      string      `json:"syllabus" gorm:"column:syllabus;type:text"`
	Textbook      string      `json:"textbook" gorm:"column:textbook;size:255"`
	Prerequisites string      `json:"prerequisites" gorm:"column:prerequisites;type:text"`
	IsCompulsory  bool        `json:"isCompulsory" gorm:"column:is_compulsory;default:true"`
	StatusID      int         `json:"statusId" gorm:"column:status_id"`
	
	Department *Department `json:"department,omitempty" gorm:"-"`
	Major      *Major      `json:"major,omitempty" gorm:"-"`
	CourseType *CourseType `json:"courseType,omitempty" gorm:"-"`
	Status     *StatusType `json:"status,omitempty" gorm:"-"`
}

func (Course) TableName() string {
	return "courses"
}
