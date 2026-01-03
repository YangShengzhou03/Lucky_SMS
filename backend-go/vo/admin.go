package vo

import "lucky-sms/models"

type AdminHomeVO struct {
	TotalStudents    int `json:"totalStudents"`
	TotalTeachers    int `json:"totalTeachers"`
	TotalCourses     int `json:"totalCourses"`
	TotalDepartments int `json:"totalDepartments"`
	TotalMajors      int `json:"totalMajors"`
	TotalClasses     int `json:"totalClasses"`
}

type UserManagementVO struct {
	UserID     int    `json:"userId"`
	Username   string `json:"username"`
	RealName   string `json:"realName"`
	Phone      string `json:"phone"`
	Email      string `json:"email"`
	UserTypeID  int    `json:"userTypeId"`
	StatusID   int    `json:"statusId"`
	Status     *models.StatusType `json:"status,omitempty"`
}

type CourseManagementVO struct {
	CourseID       int     `json:"courseId"`
	CourseCode     string  `json:"courseCode"`
	CourseName     string  `json:"courseName"`
	CourseTypeID   int     `json:"courseTypeId"`
	Credit         float64 `json:"credit"`
	TotalHours     int     `json:"totalHours"`
	TheoryHours    int     `json:"theoryHours"`
	PracticeHours  int     `json:"practiceHours"`
	DepartmentID   int     `json:"departmentId"`
	MajorID        int     `json:"majorId"`
	IsCompulsory   bool    `json:"isCompulsory"`
	StatusID       int     `json:"statusId"`
	Department     *models.Department `json:"department,omitempty"`
	Major          *models.Major      `json:"major,omitempty"`
	CourseType     *models.CourseType `json:"courseType,omitempty"`
	Status         *models.StatusType `json:"status,omitempty"`
}

type DepartmentManagementVO struct {
	DepartmentID   int    `json:"departmentId"`
	DepartmentCode string `json:"departmentCode"`
	DepartmentName string `json:"departmentName"`
	Description    string `json:"description"`
	StatusID       int    `json:"statusId"`
	Status         *models.StatusType `json:"status,omitempty"`
}

type GradeManagementVO struct {
	GradeID        int     `json:"gradeId"`
	StudentID      int     `json:"studentId"`
	StudentNo      string  `json:"studentNo"`
	StudentName    string  `json:"studentName"`
	CourseID       int     `json:"courseId"`
	CourseCode     string  `json:"courseCode"`
	CourseName     string  `json:"courseName"`
	SemesterID     int     `json:"semesterId"`
	SemesterName   string  `json:"semesterName"`
	RegularScore   float64 `json:"regularScore"`
	FinalScore     float64 `json:"finalScore"`
	TotalScore     float64 `json:"totalScore"`
	GradePoint     float64 `json:"gradePoint"`
	GradeLevel     string  `json:"gradeLevel"`
	StatusID       int     `json:"statusId"`
	GradedAt       string  `json:"gradedAt"`
}

type AnnouncementManagementVO struct {
	AnnouncementID   int    `json:"announcementId"`
	Title            string `json:"title"`
	Content          string `json:"content"`
	AnnouncementType int    `json:"announcementType"`
	PublishDate      string `json:"publishDate"`
	ExpiryDate       string `json:"expiryDate"`
	Priority         int    `json:"priority"`
	StatusID         int    `json:"statusId"`
	AnnouncementTypeObj *models.AnnouncementType `json:"announcementTypeObj,omitempty"`
	Status           *models.StatusType `json:"status,omitempty"`
}

type SystemSettingsVO struct {
	Key   string `json:"key"`
	Value string `json:"value"`
	Description string `json:"description"`
}
