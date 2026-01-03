package middleware

import (
	"lucky-sms/config"
	"lucky-sms/utils"
	"strings"

	"github.com/gin-gonic/gin"
)

func CORS() gin.HandlerFunc {
	return func(c *gin.Context) {
		cfg := config.GlobalConfig.CORS

		origin := c.Request.Header.Get("Origin")
		allowed := false

		for _, allowedOrigin := range cfg.AllowedOrigins {
			if origin == allowedOrigin || allowedOrigin == "*" {
				allowed = true
				break
			}
		}

		if allowed {
			c.Writer.Header().Set("Access-Control-Allow-Origin", origin)
		}

		c.Writer.Header().Set("Access-Control-Allow-Credentials", "true")
		c.Writer.Header().Set("Access-Control-Allow-Headers", cfg.AllowedHeaders)
		c.Writer.Header().Set("Access-Control-Allow-Methods", strings.Join(cfg.AllowedMethods, ", "))
		c.Writer.Header().Set("Access-Control-Expose-Headers", "Content-Length, Content-Type")

		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(204)
			return
		}

		c.Next()
	}
}
