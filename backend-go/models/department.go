package models

type Department struct {
	BaseEntity
	DepartmentID   int    `json:"departmentId" gorm:"column:department_id;primaryKey;autoIncrement"`
	DepartmentCode string `json:"departmentCode" gorm:"column:department_code;size:20;uniqueIndex"`
	DepartmentName string `json:"departmentName" gorm:"column:department_name;size:100"`
	Description    string `json:"description" gorm:"column:description;type:text"`
	StatusID       int    `json:"statusId" gorm:"column:status_id"`

	Status *StatusType `json:"status,omitempty" gorm:"-"`
}

func (Department) TableName() string {
	return "departments"
}
