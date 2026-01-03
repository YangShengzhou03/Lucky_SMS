package models

import "time"

type BaseEntity struct {
	CreatedBy   *int       `json:"createdBy" gorm:"column:created_by"`
	UpdatedBy   *int       `json:"updatedBy" gorm:"column:updated_by"`
	CreatedAt   time.Time  `json:"createdAt" gorm:"column:created_at;autoCreateTime"`
	UpdatedAt   time.Time  `json:"updatedAt" gorm:"column:updated_at;autoUpdateTime"`
	DeletedAt   *time.Time `json:"deletedAt,omitempty" gorm:"column:deleted_at;index"`
}

func (BaseEntity) TableName() string {
	return ""
}
