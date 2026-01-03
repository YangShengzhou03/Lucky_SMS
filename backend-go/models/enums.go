package models

type StatusType struct {
	StatusID   int    `json:"statusId" gorm:"column:status_id;primaryKey;autoIncrement"`
	StatusName string `json:"statusName" gorm:"column:status_name;size:50"`
	StatusDesc string `json:"statusDesc" gorm:"column:status_desc;size:200"`
}

func (StatusType) TableName() string {
	return "status_types"
}

type SemesterType struct {
	SemesterTypeID   int    `json:"semesterTypeId" gorm:"column:semester_type_id;primaryKey;autoIncrement"`
	SemesterTypeName string `json:"semesterTypeName" gorm:"column:semester_type_name;size:50"`
}

func (SemesterType) TableName() string {
	return "semester_types"
}

type CourseType struct {
	CourseTypeID   int    `json:"courseTypeId" gorm:"column:course_type_id;primaryKey;autoIncrement"`
	CourseTypeName string `json:"courseTypeName" gorm:"column:course_type_name;size:50"`
}

func (CourseType) TableName() string {
	return "course_types"
}

type SelectionType struct {
	SelectionTypeID   int    `json:"selectionTypeId" gorm:"column:selection_type_id;primaryKey;autoIncrement"`
	SelectionTypeName string `json:"selectionTypeName" gorm:"column:selection_type_name;size:50"`
}

func (SelectionType) TableName() string {
	return "selection_types"
}

type AnnouncementType struct {
	AnnouncementTypeID   int    `json:"announcementTypeId" gorm:"column:announcement_type_id;primaryKey;autoIncrement"`
	AnnouncementTypeName string `json:"announcementTypeName" gorm:"column:announcement_type_name;size:50"`
}

func (AnnouncementType) TableName() string {
	return "announcement_types"
}

type Title struct {
	TitleID   int    `json:"titleId" gorm:"column:title_id;primaryKey;autoIncrement"`
	TitleName string `json:"titleName" gorm:"column:title_name;size:50"`
}

func (Title) TableName() string {
	return "titles"
}
