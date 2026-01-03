package config

import (
	"fmt"
	"log"
	"lucky-sms/models"

	"gorm.io/driver/mysql"
	"gorm.io/gorm"
	"gorm.io/gorm/logger"
)

type Config struct {
	Server   ServerConfig
	Database DatabaseConfig
	JWT      JWTConfig
	CORS     CORSConfig
	Upload   UploadConfig
	Log      LogConfig
}

type ServerConfig struct {
	Port int    `mapstructure:"port"`
	Mode string `mapstructure:"mode"`
}

type DatabaseConfig struct {
	Host      string `mapstructure:"host"`
	Port      int    `mapstructure:"port"`
	Username  string `mapstructure:"username"`
	Password  string `mapstructure:"password"`
	DBName    string `mapstructure:"dbname"`
	Charset   string `mapstructure:"charset"`
	ParseTime bool   `mapstructure:"parseTime"`
	Loc       string `mapstructure:"loc"`
}

type JWTConfig struct {
	Secret     string `mapstructure:"secret"`
	Expiration int64  `mapstructure:"expiration"`
	Issuer     string `mapstructure:"issuer"`
}

type CORSConfig struct {
	AllowedOrigins   []string `mapstructure:"allowedOrigins"`
	AllowedMethods   []string `mapstructure:"allowedMethods"`
	AllowedHeaders   string   `mapstructure:"allowedHeaders"`
	AllowCredentials bool     `mapstructure:"allowCredentials"`
}

type UploadConfig struct {
	MaxSize           int64    `mapstructure:"maxSize"`
	AllowedExtensions []string `mapstructure:"allowedExtensions"`
}

type LogConfig struct {
	Level      string `mapstructure:"level"`
	Filename   string `mapstructure:"filename"`
	MaxSize    int    `mapstructure:"maxSize"`
	MaxBackups int    `mapstructure:"maxBackups"`
	MaxAge     int    `mapstructure:"maxAge"`
	Compress   bool   `mapstructure:"compress"`
}

var GlobalConfig *Config
var DB *gorm.DB

func InitDatabase(cfg *DatabaseConfig) error {
	dsn := fmt.Sprintf("%s:%s@tcp(%s:%d)/%s?charset=%s&parseTime=%t&loc=%s",
		cfg.Username,
		cfg.Password,
		cfg.Host,
		cfg.Port,
		cfg.DBName,
		cfg.Charset,
		cfg.ParseTime,
		cfg.Loc,
	)

	var err error
	DB, err = gorm.Open(mysql.Open(dsn), &gorm.Config{
		Logger: logger.Default.LogMode(logger.Info),
	})
	if err != nil {
		return fmt.Errorf("failed to connect to database: %v", err)
	}

	sqlDB, err := DB.DB()
	if err != nil {
		return fmt.Errorf("failed to get database instance: %v", err)
	}

	sqlDB.SetMaxIdleConns(10)
	sqlDB.SetMaxOpenConns(100)

	log.Println("Database connected successfully")
	return nil
}

func AutoMigrate() error {
	return DB.AutoMigrate(
		&models.User{},
		&models.Student{},
		&models.Teacher{},
		&models.Course{},
		&models.CourseSelection{},
		&models.TeachingAssignment{},
		&models.CourseGrade{},
		&models.Department{},
		&models.Major{},
		&models.Class{},
		&models.Semester{},
		&models.Announcement{},
		&models.StatusType{},
		&models.SemesterType{},
		&models.CourseType{},
		&models.SelectionType{},
		&models.AnnouncementType{},
		&models.Title{},
	)
}
