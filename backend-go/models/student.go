package models

import (
	"math/big"
	"time"
)

type Student struct {
	BaseEntity
	StudentID          int          `json:"studentId" gorm:"column:student_id;primaryKey;autoIncrement"`
	StudentNo          string       `json:"studentNo" gorm:"column:student_no;size:20;uniqueIndex"`
	UserID             int          `json:"userId" gorm:"column:user_id;index"`
	DepartmentID       int          `json:"departmentId" gorm:"column:department_id;index"`
	MajorID            int          `json:"majorId" gorm:"column:major_id;index"`
	ClassID            int          `json:"classId" gorm:"column:class_id;index"`
	EnrollmentDate     *time.Time   `json:"enrollmentDate" gorm:"column:enrollment_date"`
	GraduationDate     *time.Time   `json:"graduationDate" gorm:"column:graduation_date"`
	EducationYears     int          `json:"educationYears" gorm:"column:education_years"`
	EducationLevelID   int          `json:"educationLevelId" gorm:"column:education_level_id"`
	StatusID           int          `json:"statusId" gorm:"column:status_id"`
	EmergencyContact   string       `json:"emergencyContact" gorm:"column:emergency_contact;size:50"`
	EmergencyPhone     string       `json:"emergencyPhone" gorm:"column:emergency_phone;size:20"`
	ParentName         string       `json:"parentName" gorm:"column:parent_name;size:50"`
	ParentPhone        string       `json:"parentPhone" gorm:"column:parent_phone;size:20"`
	HomeAddress        string       `json:"homeAddress" gorm:"column:home_address;size:255"`
	HighSchool         string       `json:"highSchool" gorm:"column:high_school;size:100"`
	EntranceScore      *big.Float   `json:"entranceScore" gorm:"column:entrance_score;type:decimal(5,2)"`
	ScholarshipInfo    string       `json:"scholarshipInfo" gorm:"column:scholarship_info;type:text"`
	
	User       *User       `json:"user,omitempty" gorm:"-"`
	Department *Department `json:"department,omitempty" gorm:"-"`
	Major      *Major      `json:"major,omitempty" gorm:"-"`
	ClassInfo  *Class      `json:"classInfo,omitempty" gorm:"-"`
	Status     *StatusType `json:"status,omitempty" gorm:"-"`
}

func (Student) TableName() string {
	return "students"
}
