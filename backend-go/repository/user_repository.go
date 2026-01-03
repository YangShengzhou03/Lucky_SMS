package repository

import (
	"lucky-sms/config"
	"lucky-sms/models"
	"lucky-sms/vo"
)

type UserRepository struct {
}

func NewUserRepository() *UserRepository {
	return &UserRepository{}
}

func (r *UserRepository) LoginByPhone(phone string) (*vo.LoginVO, error) {
	var result struct {
		UID   int    `json:"uid"`
		Phone string `json:"phone"`
		Role  string `json:"role"`
	}

	err := config.DB.Table("users AS u").
		Select("u.user_id AS uid, u.phone, ut.type_code AS role").
		Joins("LEFT JOIN user_types ut ON u.user_type_id = ut.user_type_id").
		Where("u.phone = ? AND u.deleted_at IS NULL", phone).
		First(&result).Error

	if err != nil {
		return nil, err
	}

	return &vo.LoginVO{
		UID:   result.UID,
		Phone: result.Phone,
		Role:  result.Role,
	}, nil
}

func (r *UserRepository) RegisterByPhone(phone string) (int64, error) {
	result := config.DB.Exec(
		"INSERT INTO users (phone, username, password, user_type_id, status_id) VALUES (?, ?, ?, 3, 1)",
		phone, "user_"+phone, "123456",
	)
	return result.RowsAffected, result.Error
}

func (r *UserRepository) ResetPasswordByPhone(phone, password string) (int64, error) {
	result := config.DB.Table("users").
		Where("phone = ? AND deleted_at IS NULL", phone).
		Update("password", password)
	return result.RowsAffected, result.Error
}

func (r *UserRepository) LoginByPassword(phone, password string) (*vo.LoginVO, error) {
	var result struct {
		UID   int    `json:"uid"`
		Phone string `json:"phone"`
		Role  string `json:"role"`
	}

	err := config.DB.Table("users AS u").
		Select("u.user_id AS uid, u.phone, ut.type_code AS role").
		Joins("LEFT JOIN user_types ut ON u.user_type_id = ut.user_type_id").
		Where("u.phone = ? AND u.password = ? AND u.deleted_at IS NULL", phone, password).
		First(&result).Error

	if err != nil {
		return nil, err
	}

	return &vo.LoginVO{
		UID:   result.UID,
		Phone: result.Phone,
		Role:  result.Role,
	}, nil
}

func (r *UserRepository) GetUserList(keyword, role string, page, pageSize int) ([]map[string]interface{}, int64, error) {
	var users []map[string]interface{}
	var total int64

	query := config.DB.Table("users AS u").
		Select("u.*, ut.type_name as role_name, d.department_name").
		Joins("LEFT JOIN user_types ut ON u.user_type_id = ut.user_type_id").
		Joins("LEFT JOIN departments d ON u.user_id = d.dean_user_id").
		Where("u.deleted_at IS NULL")

	if keyword != "" {
		query = query.Where("u.username LIKE ? OR u.real_name LIKE ? OR u.phone LIKE ?",
			"%"+keyword+"%", "%"+keyword+"%", "%"+keyword+"%")
	}

	if role != "" {
		query = query.Where("ut.type_code = ?", role)
	}

	query.Count(&total)

	err := query.Offset((page - 1) * pageSize).
		Limit(pageSize).
		Find(&users).Error

	return users, total, err
}

func (r *UserRepository) GetTotalUsers() (int64, error) {
	var count int64
	err := config.DB.Table("users").Where("deleted_at IS NULL").Count(&count).Error
	return count, err
}

func (r *UserRepository) GetTotalStudents() (int64, error) {
	var count int64
	err := config.DB.Table("users AS u").
		Joins("INNER JOIN user_types ut ON u.user_type_id = ut.user_type_id").
		Where("ut.type_code = 'STUDENT' AND u.deleted_at IS NULL").
		Count(&count).Error
	return count, err
}

func (r *UserRepository) GetTotalTeachers() (int64, error) {
	var count int64
	err := config.DB.Table("users AS u").
		Joins("INNER JOIN user_types ut ON u.user_type_id = ut.user_type_id").
		Where("ut.type_code = 'TEACHER' AND u.deleted_at IS NULL").
		Count(&count).Error
	return count, err
}

func (r *UserRepository) GetTotalCourses() (int64, error) {
	var count int64
	err := config.DB.Table("courses").Where("deleted_at IS NULL").Count(&count).Error
	return count, err
}

func (r *UserRepository) GetTotalDepartments() (int64, error) {
	var count int64
	err := config.DB.Table("departments").Where("deleted_at IS NULL").Count(&count).Error
	return count, err
}

func (r *UserRepository) GetByID(id int) (*models.User, error) {
	var user models.User
	err := config.DB.Where("user_id = ? AND deleted_at IS NULL", id).First(&user).Error
	if err != nil {
		return nil, err
	}
	return &user, nil
}

func (r *UserRepository) Create(user *models.User) error {
	return config.DB.Create(user).Error
}

func (r *UserRepository) Update(user *models.User) error {
	return config.DB.Save(user).Error
}

func (r *UserRepository) Delete(id int) error {
	return config.DB.Delete(&models.User{}, id).Error
}
