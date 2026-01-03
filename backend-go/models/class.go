package models

type Class struct {
	BaseEntity
	ClassID      int    `json:"classId" gorm:"column:class_id;primaryKey;autoIncrement"`
	ClassCode    string `json:"classCode" gorm:"column:class_code;size:20;uniqueIndex"`
	ClassName    string `json:"className" gorm:"column:class_name;size:100"`
	DepartmentID int    `json:"departmentId" gorm:"column:department_id;index"`
	MajorID      int    `json:"majorId" gorm:"column:major_id;index"`
	EnrollmentYear int  `json:"enrollmentYear" gorm:"column:enrollment_year"`
	StatusID     int    `json:"statusId" gorm:"column:status_id"`
	
	Department *Department `json:"department,omitempty" gorm:"-"`
	Major      *Major      `json:"major,omitempty" gorm:"-"`
	Status     *StatusType `json:"status,omitempty" gorm:"-"`
}

func (Class) TableName() string {
	return "classes"
}
