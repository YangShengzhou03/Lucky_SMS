package vo

import "lucky-sms/models"

type TeacherVO struct {
	TeacherID           int                `json:"teacherId"`
	TeacherNo           string             `json:"teacherNo"`
	UserID              int                `json:"userId"`
	DepartmentID        int                `json:"departmentId"`
	TitleID             int                `json:"titleId"`
	HireDate            string             `json:"hireDate"`
	OfficeLocation      string             `json:"officeLocation"`
	Phone               string             `json:"phone"`
	Email               string             `json:"email"`
	ResearchDirection   string             `json:"researchDirection"`
	EducationBackground string             `json:"educationBackground"`
	WorkExperience      string             `json:"workExperience"`
	StatusID            int                `json:"statusId"`
	User                *models.User       `json:"user,omitempty"`
	Department          *models.Department `json:"department,omitempty"`
	Title               *models.Title      `json:"title,omitempty"`
	Status              *models.StatusType `json:"status,omitempty"`
}

type TeacherProfileVO struct {
	TeacherID           int    `json:"teacherId"`
	TeacherNo           string `json:"teacherNo"`
	UserID              int    `json:"userId"`
	RealName            string `json:"realName"`
	GenderID            int    `json:"genderId"`
	Birthday            string `json:"birthday"`
	Phone               string `json:"phone"`
	Email               string `json:"email"`
	Address             string `json:"address"`
	DepartmentID        int    `json:"departmentId"`
	TitleID             int    `json:"titleId"`
	HireDate            string `json:"hireDate"`
	OfficeLocation      string `json:"officeLocation"`
	ResearchDirection   string `json:"researchDirection"`
	EducationBackground string `json:"educationBackground"`
	WorkExperience      string `json:"workExperience"`
}

type HomeVO struct {
	TeacherName     string `json:"teacherName"`
	TeacherNo       string `json:"teacherNo"`
	TitleName       string `json:"titleName"`
	DepartmentName  string `json:"departmentName"`
}

type CourseManagementVO struct {
	CourseID        int     `json:"courseId"`
	CourseCode      string  `json:"courseCode"`
	CourseName      string  `json:"courseName"`
	Credit          float64 `json:"credit"`
	TotalHours      int     `json:"totalHours"`
	TheoryHours     int     `json:"theoryHours"`
	PracticeHours   int     `json:"practiceHours"`
	SelectedCount   int     `json:"selectedCount"`
	SemesterID      int     `json:"semesterId"`
	SemesterName    string  `json:"semesterName"`
}

type GradeManagementVO struct {
	GradeID        int     `json:"gradeId"`
	StudentID      int     `json:"studentId"`
	StudentNo      string  `json:"studentNo"`
	StudentName    string  `json:"studentName"`
	CourseID       int     `json:"courseId"`
	CourseCode     string  `json:"courseCode"`
	CourseName     string  `json:"courseName"`
	RegularScore   float64 `json:"regularScore"`
	FinalScore     float64 `json:"finalScore"`
	TotalScore     float64 `json:"totalScore"`
	GradePoint     float64 `json:"gradePoint"`
	GradeLevel     string  `json:"gradeLevel"`
	GradedAt       string  `json:"gradedAt"`
}

type StudentManagementVO struct {
	StudentID      int    `json:"studentId"`
	StudentNo      string `json:"studentNo"`
	RealName       string `json:"realName"`
	ClassID        int    `json:"classId"`
	ClassName      string `json:"className"`
	MajorID        int    `json:"majorId"`
	MajorName      string `json:"majorName"`
	DepartmentID   int    `json:"departmentId"`
	DepartmentName string `json:"departmentName"`
}

type ScheduleVO struct {
	CourseID      int    `json:"courseId"`
	CourseCode    string `json:"courseCode"`
	CourseName    string `json:"courseName"`
	SemesterID    int    `json:"semesterId"`
	SemesterName  string `json:"semesterName"`
	SelectedCount int    `json:"selectedCount"`
}
