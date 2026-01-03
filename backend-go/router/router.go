package router

import (
	"lucky-sms/handler"
	"lucky-sms/middleware"

	"github.com/gin-gonic/gin"
)

func SetupRouter() *gin.Engine {
	r := gin.Default()

	r.Use(middleware.Recovery())
	r.Use(middleware.ErrorHandler())
	r.Use(middleware.CORS())

	userHandler := handler.NewUserHandler()
	studentHandler := handler.NewStudentHandler()
	teacherHandler := handler.NewTeacherHandler()
	adminHandler := handler.NewAdminHandler()

	api := r.Group("/api")
	{
		login := api.Group("/login")
		{
			login.POST("/phone", userHandler.LoginByPhone)
			login.POST("/password", userHandler.LoginByPassword)
		}

		reset := api.Group("/reset-password")
		{
			reset.POST("", userHandler.ResetPassword)
		}

		student := api.Group("/student")
		student.Use(middleware.Auth())
		{
			student.GET("/home", studentHandler.GetHome)
			student.GET("/grades", studentHandler.GetGrades)
			student.GET("/courses/available", studentHandler.GetAvailableCourses)
			student.POST("/courses/select", studentHandler.SelectCourse)
			student.POST("/courses/drop", studentHandler.DropCourse)
			student.GET("/courses/statistics", studentHandler.GetCourseSelectionStatistics)
			student.GET("/profile", studentHandler.GetProfile)
			student.POST("/profile", studentHandler.UpdateProfile)
			student.GET("/status", studentHandler.GetStatus)
		}

		teacher := api.Group("/teacher")
		teacher.Use(middleware.Auth())
		{
			teacher.GET("/home", teacherHandler.GetHome)
			teacher.GET("/profile", teacherHandler.GetProfile)
			teacher.GET("/courses", teacherHandler.GetCourses)
			teacher.GET("/courses/:courseId/students", teacherHandler.GetCourseStudents)
			teacher.GET("/courses/:courseId/grades", teacherHandler.GetCourseGrades)
			teacher.PUT("/grades/:gradeId", teacherHandler.UpdateGrade)
			teacher.GET("/schedule", teacherHandler.GetSchedule)
		}

		admin := api.Group("/admin")
		admin.Use(middleware.Auth())
		{
			admin.GET("/home", adminHandler.GetHome)

			courses := admin.Group("/courses")
			{
				courses.GET("", adminHandler.GetCourseList)
				courses.POST("", adminHandler.CreateCourse)
				courses.PUT("", adminHandler.UpdateCourse)
				courses.DELETE("/:courseId", adminHandler.DeleteCourse)
			}

			departments := admin.Group("/departments")
			{
				departments.GET("", adminHandler.GetDepartmentList)
				departments.POST("", adminHandler.CreateDepartment)
				departments.PUT("", adminHandler.UpdateDepartment)
				departments.DELETE("/:deptId", adminHandler.DeleteDepartment)
			}

			announcements := admin.Group("/announcements")
			{
				announcements.GET("", adminHandler.GetAnnouncementList)
				announcements.POST("", adminHandler.CreateAnnouncement)
				announcements.PUT("", adminHandler.UpdateAnnouncement)
				announcements.DELETE("/:announcementId", adminHandler.DeleteAnnouncement)
			}

			grades := admin.Group("/grades")
			{
				grades.GET("", adminHandler.GetGradeList)
				grades.POST("", adminHandler.CreateGrade)
				grades.PUT("", adminHandler.UpdateGrade)
				grades.DELETE("/:gradeId", adminHandler.DeleteGrade)
			}
		}
	}

	return r
}
