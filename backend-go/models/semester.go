package models

type Semester struct {
	BaseEntity
	SemesterID      int    `json:"semesterId" gorm:"column:semester_id;primaryKey;autoIncrement"`
	SemesterCode    string `json:"semesterCode" gorm:"column:semester_code;size:20;uniqueIndex"`
	SemesterName    string `json:"semesterName" gorm:"column:semester_name;size:100"`
	SemesterTypeID  int    `json:"semesterTypeId" gorm:"column:semester_type_id;index"`
	StartDate       string `json:"startDate" gorm:"column:start_date;type:date"`
	EndDate         string `json:"endDate" gorm:"column:end_date;type:date"`
	IsCurrent       bool   `json:"isCurrent" gorm:"column:is_current;default:false"`
	StatusID        int    `json:"statusId" gorm:"column:status_id"`
	
	SemesterType *SemesterType `json:"semesterType,omitempty" gorm:"-"`
	Status       *StatusType   `json:"status,omitempty" gorm:"-"`
}

func (Semester) TableName() string {
	return "semesters"
}
