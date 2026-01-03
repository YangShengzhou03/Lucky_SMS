package handler

import (
	"lucky-sms/service"
	"lucky-sms/utils"
	"strconv"

	"github.com/gin-gonic/gin"
)

type TeacherHandler struct {
	teacherService *service.TeacherService
}

func NewTeacherHandler() *TeacherHandler {
	return &TeacherHandler{
		teacherService: service.NewTeacherService(),
	}
}

func (h *TeacherHandler) GetHome(c *gin.Context) {
	userID := c.GetInt("userID")

	home, err := h.teacherService.GetTeacherHome(userID)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", home)
}

func (h *TeacherHandler) GetProfile(c *gin.Context) {
	userID := c.GetInt("userID")

	profile, err := h.teacherService.GetTeacherProfile(userID)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "获取个人信息成功", profile)
}

func (h *TeacherHandler) GetCourses(c *gin.Context) {
	userID := c.GetInt("userID")
	semesterID, _ := strconv.Atoi(c.DefaultQuery("semesterId", "1"))
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	teacher, err := h.teacherService.GetTeacherProfile(userID)
	if err != nil {
		utils.Error(c, 500, "获取教师信息失败")
		return
	}

	courses, total, err := h.teacherService.GetTeacherCourses(teacher.TeacherID, semesterID, page, pageSize)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", map[string]interface{}{
		"list":     courses,
		"total":    total,
		"page":     page,
		"pageSize": pageSize,
	})
}

func (h *TeacherHandler) GetCourseStudents(c *gin.Context) {
	courseID, _ := strconv.Atoi(c.Param("courseId"))
	semesterID, _ := strconv.Atoi(c.DefaultQuery("semesterId", "1"))
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	students, total, err := h.teacherService.GetCourseStudents(courseID, semesterID, page, pageSize)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", map[string]interface{}{
		"list":     students,
		"total":    total,
		"page":     page,
		"pageSize": pageSize,
	})
}

func (h *TeacherHandler) GetCourseGrades(c *gin.Context) {
	courseID, _ := strconv.Atoi(c.Param("courseId"))
	semesterID, _ := strconv.Atoi(c.DefaultQuery("semesterId", "1"))
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	grades, total, err := h.teacherService.GetCourseGrades(courseID, semesterID, page, pageSize)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", map[string]interface{}{
		"list":     grades,
		"total":    total,
		"page":     page,
		"pageSize": pageSize,
	})
}

func (h *TeacherHandler) UpdateGrade(c *gin.Context) {
	gradeID, _ := strconv.Atoi(c.Param("gradeId"))
	userID := c.GetInt("userID")

	var req struct {
		RegularScore float64 `json:"regularScore" binding:"required"`
		FinalScore   float64 `json:"finalScore" binding:"required"`
		TotalScore   float64 `json:"totalScore" binding:"required"`
		GradePoint   float64 `json:"gradePoint" binding:"required"`
		GradeLevel   string  `json:"gradeLevel" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	err := h.teacherService.UpdateGrade(gradeID, req.RegularScore, req.FinalScore, req.TotalScore, req.GradePoint, req.GradeLevel, userID)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "更新成绩成功", nil)
}

func (h *TeacherHandler) GetSchedule(c *gin.Context) {
	userID := c.GetInt("userID")
	semesterID, _ := strconv.Atoi(c.DefaultQuery("semesterId", "1"))
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	teacher, err := h.teacherService.GetTeacherProfile(userID)
	if err != nil {
		utils.Error(c, 500, "获取教师信息失败")
		return
	}

	schedule, total, err := h.teacherService.GetTeacherSchedule(teacher.TeacherID, semesterID, page, pageSize)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", map[string]interface{}{
		"list":     schedule,
		"total":    total,
		"page":     page,
		"pageSize": pageSize,
	})
}
