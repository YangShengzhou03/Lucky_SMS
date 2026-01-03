package models

type Announcement struct {
	BaseEntity
	AnnouncementID   int    `json:"announcementId" gorm:"column:announcement_id;primaryKey;autoIncrement"`
	Title            string `json:"title" gorm:"column:title;size:200"`
	Content          string `json:"content" gorm:"column:content;type:text"`
	AnnouncementType int    `json:"announcementType" gorm:"column:announcement_type;index"`
	PublishDate      string `json:"publishDate" gorm:"column:publish_date;type:date"`
	ExpiryDate       string `json:"expiryDate" gorm:"column:expiry_date;type:date"`
	Priority         int    `json:"priority" gorm:"column:priority;default:0"`
	StatusID         int    `json:"statusId" gorm:"column:status_id"`
	
	AnnouncementTypeObj *AnnouncementType `json:"announcementTypeObj,omitempty" gorm:"-"`
	Status              *StatusType       `json:"status,omitempty" gorm:"-"`
}

func (Announcement) TableName() string {
	return "announcements"
}
