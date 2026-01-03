package config

import (
	"fmt"
	"log"
	"os"

	"github.com/spf13/viper"
)

func LoadConfig(configPath string) (*Config, error) {
	viper.SetConfigFile(configPath)
	viper.SetConfigType("yaml")

	if err := viper.ReadInConfig(); err != nil {
		return nil, err
	}

	var cfg Config
	if err := viper.Unmarshal(&cfg); err != nil {
		return nil, err
	}

	GlobalConfig = &cfg
	return &cfg, nil
}

func LoadConfigFromEnv() (*Config, error) {
	viper.AutomaticEnv()
	viper.SetEnvPrefix("LUCKY_SMS")

	cfg := &Config{
		Server: ServerConfig{
			Port: getEnvAsInt("SERVER_PORT", 8081),
			Mode: getEnv("SERVER_MODE", "debug"),
		},
		Database: DatabaseConfig{
			Host:     getEnv("DB_HOST", "localhost"),
			Port:     getEnvAsInt("DB_PORT", 3306),
			Username: getEnv("DB_USERNAME", "root"),
			Password: getEnv("DB_PASSWORD", "root"),
			DBName:   getEnv("DB_NAME", "lucky_sms"),
			Charset:  getEnv("DB_CHARSET", "utf8mb4"),
			ParseTime: true,
			Loc:      "Local",
		},
		JWT: JWTConfig{
			Secret:     getEnv("JWT_SECRET", "3f7d2a9b5e8c1d4f6h0j2k7m9n1p3q5r8t0v2x4y6z8b0d2f4h6j8l0n2p"),
			Expiration: getEnvAsInt64("JWT_EXPIRATION", 86400000),
			Issuer:     getEnv("JWT_ISSUER", "lucky-sms"),
		},
		CORS: CORSConfig{
			AllowedOrigins:   []string{"http://localhost:8080", "http://127.0.0.1:8080"},
			AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
			AllowedHeaders:   "*",
			AllowCredentials: true,
		},
		Upload: UploadConfig{
			MaxSize:           10485760,
			AllowedExtensions: []string{".jpg", ".jpeg", ".png", ".gif", ".pdf", ".doc", ".docx"},
		},
		Log: LogConfig{
			Level:      getEnv("LOG_LEVEL", "debug"),
			Filename:   getEnv("LOG_FILENAME", "logs/lucky-sms.log"),
			MaxSize:    getEnvAsInt("LOG_MAX_SIZE", 10),
			MaxBackups: getEnvAsInt("LOG_MAX_BACKUPS", 30),
			MaxAge:     getEnvAsInt("LOG_MAX_AGE", 30),
			Compress:   true,
		},
	}

	GlobalConfig = cfg
	return cfg, nil
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}

func getEnvAsInt(key string, defaultValue int) int {
	if value := os.Getenv(key); value != "" {
		var intValue int
		if _, err := fmt.Sscanf(value, "%d", &intValue); err == nil {
			return intValue
		}
	}
	return defaultValue
}

func getEnvAsInt64(key string, defaultValue int64) int64 {
	if value := os.Getenv(key); value != "" {
		var intValue int64
		if _, err := fmt.Sscanf(value, "%d", &intValue); err == nil {
			return intValue
		}
	}
	return defaultValue
}
