package middleware

import (
	"lucky-sms/utils"
	"strings"

	"github.com/gin-gonic/gin"
)

func Auth() gin.HandlerFunc {
	return func(c *gin.Context) {
		authHeader := c.GetHeader("Authorization")
		if authHeader == "" {
			utils.Unauthorized(c, "请携带有效的 Token（格式：Bearer <token>）")
			c.Abort()
			return
		}

		parts := strings.SplitN(authHeader, " ", 2)
		if len(parts) != 2 || parts[0] != "Bearer" {
			utils.Unauthorized(c, "Token 格式错误")
			c.Abort()
			return
		}

		token := parts[1]

		userID, err := utils.GetUserIDFromToken(token)
		if err != nil {
			utils.Unauthorized(c, "Token 无效或已过期")
			c.Abort()
			return
		}

		c.Set("userID", userID)
		c.Next()
	}
}
