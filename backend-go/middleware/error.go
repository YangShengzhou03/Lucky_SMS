package middleware

import (
	"lucky-sms/utils"
	"log"

	"github.com/gin-gonic/gin"
)

func Recovery() gin.HandlerFunc {
	return func(c *gin.Context) {
		defer func() {
			if err := recover(); err != nil {
				log.Printf("Panic recovered: %v", err)
				utils.InternalServerError(c, "系统异常")
				c.Abort()
			}
		}()
		c.Next()
	}
}

func ErrorHandler() gin.HandlerFunc {
	return func(c *gin.Context) {
		c.Next()

		if len(c.Errors) > 0 {
			err := c.Errors.Last()
			log.Printf("Error: %v", err.Error)
			utils.Error(c, 500, err.Error())
		}
	}
}
