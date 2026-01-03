package handler

import (
	"lucky-sms/service"
	"lucky-sms/utils"
	"strconv"

	"github.com/gin-gonic/gin"
)

type StudentHandler struct {
	studentService *service.StudentService
}

func NewStudentHandler() *StudentHandler {
	return &StudentHandler{
		studentService: service.NewStudentService(),
	}
}

func (h *StudentHandler) GetHome(c *gin.Context) {
	userID := c.GetInt("userID")

	home, err := h.studentService.GetStudentHome(userID)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", home)
}

func (h *StudentHandler) GetGrades(c *gin.Context) {
	userID := c.GetInt("userID")
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	grades, total, err := h.studentService.GetStudentGrades(userID, page, pageSize)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", map[string]interface{}{
		"list":  grades,
		"total": total,
		"page":  page,
		"pageSize": pageSize,
	})
}

func (h *StudentHandler) GetAvailableCourses(c *gin.Context) {
	userID := c.GetInt("userID")
	semesterID, _ := strconv.Atoi(c.DefaultQuery("semesterId", "1"))
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	courses, total, err := h.studentService.GetAvailableCourses(userID, semesterID, page, pageSize)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "获取课程列表成功", map[string]interface{}{
		"list":  courses,
		"total": total,
		"page":  page,
		"pageSize": pageSize,
	})
}

func (h *StudentHandler) SelectCourse(c *gin.Context) {
	userID := c.GetInt("userID")
	var req struct {
		CourseID   int `json:"courseId" binding:"required"`
		SemesterID int `json:"semesterId" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	student, err := h.studentService.GetStudentProfile(userID)
	if err != nil {
		utils.Error(c, 500, "获取学生信息失败")
		return
	}

	result, err := h.studentService.SelectCourse(student.StudentID, req.CourseID, req.SemesterID)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "选课成功", result)
}

func (h *StudentHandler) DropCourse(c *gin.Context) {
	userID := c.GetInt("userID")
	var req struct {
		CourseID int `json:"courseId" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	student, err := h.studentService.GetStudentProfile(userID)
	if err != nil {
		utils.Error(c, 500, "获取学生信息失败")
		return
	}

	result, err := h.studentService.DropCourse(student.StudentID, req.CourseID)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "退课成功", result)
}

func (h *StudentHandler) GetCourseSelectionStatistics(c *gin.Context) {
	userID := c.GetInt("userID")

	stats, err := h.studentService.GetCourseSelectionStatistics(userID)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", stats)
}

func (h *StudentHandler) GetProfile(c *gin.Context) {
	userID := c.GetInt("userID")

	profile, err := h.studentService.GetStudentProfile(userID)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "获取个人信息成功", profile)
}

func (h *StudentHandler) UpdateProfile(c *gin.Context) {
	userID := c.GetInt("userID")
	var req map[string]interface{}

	if err := c.ShouldBindJSON(&req); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	err := h.studentService.UpdateStudentProfile(userID, req)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "更新个人信息成功", nil)
}

func (h *StudentHandler) GetStatus(c *gin.Context) {
	userID := c.GetInt("userID")

	status, err := h.studentService.GetStudentStatus(userID)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", status)
}
