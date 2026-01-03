package models

type Major struct {
	BaseEntity
	MajorID       int    `json:"majorId" gorm:"column:major_id;primaryKey;autoIncrement"`
	MajorCode     string `json:"majorCode" gorm:"column:major_code;size:20;uniqueIndex"`
	MajorName     string `json:"majorName" gorm:"column:major_name;size:100"`
	DepartmentID  int    `json:"departmentId" gorm:"column:department_id;index"`
	Description   string `json:"description" gorm:"column:description;type:text"`
	StatusID      int    `json:"statusId" gorm:"column:status_id"`
	
	Department *Department `json:"department,omitempty" gorm:"-"`
	Status     *StatusType `json:"status,omitempty" gorm:"-"`
}

func (Major) TableName() string {
	return "majors"
}
