package vo

import "lucky-sms/models"

type StudentVO struct {
	StudentID        int                `json:"studentId"`
	StudentNo        string             `json:"studentNo"`
	UserID           int                `json:"userId"`
	DepartmentID     int                `json:"departmentId"`
	MajorID          int                `json:"majorId"`
	ClassID          int                `json:"classId"`
	EnrollmentDate   string             `json:"enrollmentDate"`
	GraduationDate   string             `json:"graduationDate"`
	EducationYears   int                `json:"educationYears"`
	EducationLevelID int                `json:"educationLevelId"`
	StatusID         int                `json:"statusId"`
	EmergencyContact string             `json:"emergencyContact"`
	EmergencyPhone   string             `json:"emergencyPhone"`
	ParentName       string             `json:"parentName"`
	ParentPhone      string             `json:"parentPhone"`
	HomeAddress      string             `json:"homeAddress"`
	HighSchool       string             `json:"highSchool"`
	EntranceScore    string             `json:"entranceScore"`
	ScholarshipInfo  string             `json:"scholarshipInfo"`
	User             *models.User       `json:"user,omitempty"`
	Department       *models.Department `json:"department,omitempty"`
	Major            *models.Major      `json:"major,omitempty"`
	ClassInfo        *models.Class      `json:"classInfo,omitempty"`
	Status           *models.StatusType `json:"status,omitempty"`
}

type StudentProfileVO struct {
	StudentID        int                `json:"studentId"`
	StudentNo        string             `json:"studentNo"`
	UserID           int                `json:"userId"`
	RealName         string             `json:"realName"`
	GenderID         int                `json:"genderId"`
	Birthday         string             `json:"birthday"`
	Phone            string             `json:"phone"`
	Email            string             `json:"email"`
	Address          string             `json:"address"`
	DepartmentID     int                `json:"departmentId"`
	MajorID          int                `json:"majorId"`
	ClassID          int                `json:"classId"`
	EnrollmentDate   string             `json:"enrollmentDate"`
	GraduationDate   string             `json:"graduationDate"`
	EducationYears   int                `json:"educationYears"`
	EducationLevelID int                `json:"educationLevelId"`
	StatusID         int                `json:"statusId"`
	EmergencyContact string             `json:"emergencyContact"`
	EmergencyPhone   string             `json:"emergencyPhone"`
	ParentName       string             `json:"parentName"`
	ParentPhone      string             `json:"parentPhone"`
	HomeAddress      string             `json:"homeAddress"`
	HighSchool       string             `json:"highSchool"`
	EntranceScore    string             `json:"entranceScore"`
	ScholarshipInfo  string             `json:"scholarshipInfo"`
}

type UpdateStudentProfileRequestVO struct {
	RealName         string `json:"realName" binding:"required"`
	GenderID         int    `json:"genderId"`
	Birthday         string `json:"birthday"`
	Phone            string `json:"phone"`
	Email            string `json:"email"`
	Address          string `json:"address"`
	EmergencyContact string `json:"emergencyContact"`
	EmergencyPhone   string `json:"emergencyPhone"`
	ParentName       string `json:"parentName"`
	ParentPhone      string `json:"parentPhone"`
	HomeAddress      string `json:"homeAddress"`
}

type HomeVO struct {
	StudentName string `json:"studentName"`
	StudentNo   string `json:"studentNo"`
	ClassName   string `json:"className"`
	MajorName   string `json:"majorName"`
	DepartmentName string `json:"departmentName"`
}

type GradesVO struct {
	GradeID        int     `json:"gradeId"`
	CourseID       int     `json:"courseId"`
	CourseCode     string  `json:"courseCode"`
	CourseName     string  `json:"courseName"`
	Credit         float64 `json:"credit"`
	SemesterID     int     `json:"semesterId"`
	SemesterName   string  `json:"semesterName"`
	RegularScore   float64 `json:"regularScore"`
	FinalScore     float64 `json:"finalScore"`
	TotalScore     float64 `json:"totalScore"`
	GradePoint     float64 `json:"gradePoint"`
	GradeLevel     string  `json:"gradeLevel"`
}

type CourseSelectionVO struct {
	SelectionID     int    `json:"selectionId"`
	CourseID        int    `json:"courseId"`
	CourseCode      string `json:"courseCode"`
	CourseName      string `json:"courseName"`
	Credit          float64 `json:"credit"`
	TeacherName     string `json:"teacherName"`
	TotalHours      int    `json:"totalHours"`
	SelectedCount   int    `json:"selectedCount"`
	MaxCapacity     int    `json:"maxCapacity"`
	IsSelected      bool   `json:"isSelected"`
}

type CourseSelectionResultVO struct {
	Success bool   `json:"success"`
	Message string `json:"message"`
}

type CourseSelectionStatisticsVO struct {
	TotalCredits     float64 `json:"totalCredits"`
	SelectedCourses int     `json:"selectedCourses"`
	CompulsoryCourses int   `json:"compulsoryCourses"`
	ElectiveCourses  int    `json:"electiveCourses"`
}

type StatusVO struct {
	StatusID   int    `json:"statusId"`
	StatusName string `json:"statusName"`
}

type TimeConflictVO struct {
	HasConflict bool     `json:"hasConflict"`
	Conflicts  []string `json:"conflicts"`
}
