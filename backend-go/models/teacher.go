package models

import (
	"time"
)

type Teacher struct {
	BaseEntity
	TeacherID           int          `json:"teacherId" gorm:"column:teacher_id;primaryKey;autoIncrement"`
	TeacherNo           string       `json:"teacherNo" gorm:"column:teacher_no;size:20;uniqueIndex"`
	UserID              int          `json:"userId" gorm:"column:user_id;index"`
	DepartmentID        int          `json:"departmentId" gorm:"column:department_id;index"`
	TitleID             int          `json:"titleId" gorm:"column:title_id;index"`
	HireDate            *time.Time   `json:"hireDate" gorm:"column:hire_date"`
	OfficeLocation      string       `json:"officeLocation" gorm:"column:office_location;size:100"`
	Phone               string       `json:"phone" gorm:"column:phone;size:20"`
	Email               string       `json:"email" gorm:"column:email;size:100"`
	ResearchDirection   string       `json:"researchDirection" gorm:"column:research_direction;type:text"`
	EducationBackground string       `json:"educationBackground" gorm:"column:education_background;type:text"`
	WorkExperience      string       `json:"workExperience" gorm:"column:work_experience;type:text"`
	StatusID            int          `json:"statusId" gorm:"column:status_id"`
	
	User       *User       `json:"user,omitempty" gorm:"-"`
	Department *Department `json:"department,omitempty" gorm:"-"`
	Title      *Title      `json:"title,omitempty" gorm:"-"`
	Status     *StatusType `json:"status,omitempty" gorm:"-"`
}

func (Teacher) TableName() string {
	return "teachers"
}
