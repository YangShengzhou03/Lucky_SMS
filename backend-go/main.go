package main

import (
	"fmt"
	"log"
	"lucky-sms/config"
	"lucky-sms/router"

	"github.com/gin-gonic/gin"
)

func main() {
	cfg, err := config.LoadConfig("config.yaml")
	if err != nil {
		log.Printf("Failed to load config file, using environment variables: %v", err)
		cfg, err = config.LoadConfigFromEnv()
		if err != nil {
			log.Fatalf("Failed to load config from environment: %v", err)
		}
	}

	if err := config.InitDatabase(&cfg.Database); err != nil {
		log.Fatalf("Failed to connect to database: %v", err)
	}

	if err := config.AutoMigrate(); err != nil {
		log.Fatalf("Failed to auto migrate: %v", err)
	}

	gin.SetMode(cfg.Server.Mode)
	r := router.SetupRouter()

	addr := fmt.Sprintf(":%d", cfg.Server.Port)
	log.Printf("Server starting on %s", addr)
	log.Printf("API base path: /api")

	if err := r.Run(addr); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}
