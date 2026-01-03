package handler

import (
	"lucky-sms/service"
	"lucky-sms/utils"
	"strconv"

	"github.com/gin-gonic/gin"
)

type UserHandler struct {
	userService *service.UserService
}

func NewUserHandler() *UserHandler {
	return &UserHandler{
		userService: service.NewUserService(),
	}
}

func (h *UserHandler) LoginByPhone(c *gin.Context) {
	var req struct {
		Phone   string `json:"phone" binding:"required"`
		Captcha string `json:"captcha" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	if req.Captcha != "123456" {
		utils.BadRequest(c, "无效验证码")
		return
	}

	loginVO, err := h.userService.LoginByPhone(req.Phone)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "登录成功", loginVO)
}

func (h *UserHandler) LoginByPassword(c *gin.Context) {
	var req struct {
		Phone    string `json:"phone" binding:"required"`
		Password string `json:"password" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	loginVO, err := h.userService.LoginByPassword(req.Phone, req.Password)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "登录成功", loginVO)
}

func (h *UserHandler) ResetPassword(c *gin.Context) {
	var req struct {
		Phone    string `json:"phone" binding:"required"`
		Password string `json:"password" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	result, err := h.userService.ResetPassword(req.Phone, req.Password)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "重置密码成功", result)
}
