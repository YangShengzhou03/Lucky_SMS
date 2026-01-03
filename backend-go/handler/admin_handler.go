package handler

import (
	"lucky-sms/models"
	"lucky-sms/service"
	"lucky-sms/utils"
	"strconv"

	"github.com/gin-gonic/gin"
)

type AdminHandler struct {
	adminService *service.AdminService
}

func NewAdminHandler() *AdminHandler {
	return &AdminHandler{
		adminService: service.NewAdminService(),
	}
}

func (h *AdminHandler) GetHome(c *gin.Context) {
	home, err := h.adminService.GetAdminHome()
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", home)
}

func (h *AdminHandler) GetCourseList(c *gin.Context) {
	keyword := c.Query("keyword")
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	courses, total, err := h.adminService.GetCourseList(keyword, page, pageSize)
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

func (h *AdminHandler) CreateCourse(c *gin.Context) {
	var course models.Course
	if err := c.ShouldBindJSON(&course); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	if err := h.adminService.CreateCourse(&course); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "创建课程成功", course)
}

func (h *AdminHandler) UpdateCourse(c *gin.Context) {
	var course models.Course
	if err := c.ShouldBindJSON(&course); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	if err := h.adminService.UpdateCourse(&course); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "更新课程成功", course)
}

func (h *AdminHandler) DeleteCourse(c *gin.Context) {
	courseID, _ := strconv.Atoi(c.Param("courseId"))

	if err := h.adminService.DeleteCourse(courseID); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "删除课程成功", nil)
}

func (h *AdminHandler) GetDepartmentList(c *gin.Context) {
	keyword := c.Query("keyword")
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	departments, total, err := h.adminService.GetDepartmentList(keyword, page, pageSize)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", map[string]interface{}{
		"list":     departments,
		"total":    total,
		"page":     page,
		"pageSize": pageSize,
	})
}

func (h *AdminHandler) CreateDepartment(c *gin.Context) {
	var department models.Department
	if err := c.ShouldBindJSON(&department); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	if err := h.adminService.CreateDepartment(&department); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "创建院系成功", department)
}

func (h *AdminHandler) UpdateDepartment(c *gin.Context) {
	var department models.Department
	if err := c.ShouldBindJSON(&department); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	if err := h.adminService.UpdateDepartment(&department); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "更新院系成功", department)
}

func (h *AdminHandler) DeleteDepartment(c *gin.Context) {
	deptID, _ := strconv.Atoi(c.Param("deptId"))

	if err := h.adminService.DeleteDepartment(deptID); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "删除院系成功", nil)
}

func (h *AdminHandler) GetAnnouncementList(c *gin.Context) {
	keyword := c.Query("keyword")
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	announcements, total, err := h.adminService.GetAnnouncementList(keyword, page, pageSize)
	if err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "请求成功", map[string]interface{}{
		"list":     announcements,
		"total":    total,
		"page":     page,
		"pageSize": pageSize,
	})
}

func (h *AdminHandler) CreateAnnouncement(c *gin.Context) {
	var announcement models.Announcement
	if err := c.ShouldBindJSON(&announcement); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	if err := h.adminService.CreateAnnouncement(&announcement); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "创建公告成功", announcement)
}

func (h *AdminHandler) UpdateAnnouncement(c *gin.Context) {
	var announcement models.Announcement
	if err := c.ShouldBindJSON(&announcement); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	if err := h.adminService.UpdateAnnouncement(&announcement); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "更新公告成功", announcement)
}

func (h *AdminHandler) DeleteAnnouncement(c *gin.Context) {
	announcementID, _ := strconv.Atoi(c.Param("announcementId"))

	if err := h.adminService.DeleteAnnouncement(announcementID); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "删除公告成功", nil)
}

func (h *AdminHandler) GetGradeList(c *gin.Context) {
	keyword := c.Query("keyword")
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("pageSize", "10"))

	grades, total, err := h.adminService.GetGradeList(keyword, page, pageSize)
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

func (h *AdminHandler) CreateGrade(c *gin.Context) {
	var grade models.CourseGrade
	if err := c.ShouldBindJSON(&grade); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	if err := h.adminService.CreateGrade(&grade); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "创建成绩成功", grade)
}

func (h *AdminHandler) UpdateGrade(c *gin.Context) {
	var grade models.CourseGrade
	if err := c.ShouldBindJSON(&grade); err != nil {
		utils.BadRequest(c, "参数错误")
		return
	}

	if err := h.adminService.UpdateGrade(&grade); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "更新成绩成功", grade)
}

func (h *AdminHandler) DeleteGrade(c *gin.Context) {
	gradeID, _ := strconv.Atoi(c.Param("gradeId"))

	if err := h.adminService.DeleteGrade(gradeID); err != nil {
		utils.Error(c, 500, err.Error())
		return
	}

	utils.SuccessWithMessage(c, "删除成绩成功", nil)
}
