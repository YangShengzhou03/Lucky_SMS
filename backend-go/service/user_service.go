package service

import (
	"errors"
	"lucky-sms/repository"
	"lucky-sms/utils"
	"lucky-sms/vo"
)

type UserService struct {
	userRepo *repository.UserRepository
}

func NewUserService() *UserService {
	return &UserService{
		userRepo: repository.NewUserRepository(),
	}
}

func (s *UserService) LoginByPhone(phone string) (*vo.LoginVO, error) {
	loginVO, err := s.userRepo.LoginByPhone(phone)
	if err != nil {
		rowsAffected, regErr := s.userRepo.RegisterByPhone(phone)
		if regErr != nil || rowsAffected == 0 {
			return nil, errors.New("注册失败")
		}
		
		loginVO, err = s.userRepo.LoginByPhone(phone)
		if err != nil {
			return nil, errors.New("用户注册成功但查询失败")
		}
	}

	token, err := utils.GenerateToken(loginVO.UID)
	if err != nil {
		return nil, err
	}

	loginVO.Token = token
	loginVO.Phone = utils.DesensitizePhone(loginVO.Phone)

	return loginVO, nil
}

func (s *UserService) LoginByPassword(phone, password string) (*vo.LoginVO, error) {
	loginVO, err := s.userRepo.LoginByPassword(phone, password)
	if err != nil {
		return nil, errors.New("用户名或密码错误")
	}

	token, err := utils.GenerateToken(loginVO.UID)
	if err != nil {
		return nil, err
	}

	loginVO.Token = token
	loginVO.Phone = utils.DesensitizePhone(loginVO.Phone)

	return loginVO, nil
}

func (s *UserService) ResetPassword(phone, password string) (*vo.ResetPasswordVO, error) {
	rowsAffected, err := s.userRepo.ResetPasswordByPhone(phone, password)
	if err != nil || rowsAffected == 0 {
		return &vo.ResetPasswordVO{Success: false}, errors.New("重置密码失败")
	}
	return &vo.ResetPasswordVO{Success: true}, nil
}

func (s *UserService) GetUserByID(id int) (*vo.UserManagementVO, error) {
	return nil, nil
}
