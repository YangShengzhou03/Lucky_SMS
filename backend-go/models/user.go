package models

import (
	"time"
)

type User struct {
	BaseEntity
	UserID     int       `json:"userId" gorm:"column:user_id;primaryKey;autoIncrement"`
	Username   string    `json:"username" gorm:"column:username;size:50;uniqueIndex"`
	Password   string    `json:"-" gorm:"column:password;size:255"`
	Phone      string    `json:"phone" gorm:"column:phone;size:20;uniqueIndex"`
	Email      string    `json:"email" gorm:"column:email;size:100"`
	Avatar     string    `json:"avatar" gorm:"column:avatar;size:255"`
	RealName   string    `json:"realName" gorm:"column:real_name;size:50"`
	GenderID   int       `json:"genderId" gorm:"column:gender_id"`
	Birthday   *time.Time `json:"birthday" gorm:"column:birthday"`
	Address    string    `json:"address" gorm:"column:address;size:255"`
	UserTypeID int       `json:"userTypeId" gorm:"column:user_type_id"`
	StatusID   int       `json:"statusId" gorm:"column:status_id"`
	
	RoleName  string     `json:"roleName,omitempty" gorm:"-"`
	Token     string     `json:"token,omitempty" gorm:"-"`
	Status    *StatusType `json:"status,omitempty" gorm:"-"`
}

func (User) TableName() string {
	return "users"
}
