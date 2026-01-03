package utils

import (
	"errors"
	"lucky-sms/config"
	"time"

	"github.com/golang-jwt/jwt/v5"
)

type Claims struct {
	UserID int `json:"userId"`
	jwt.RegisteredClaims
}

func GenerateToken(userID int) (string, error) {
	claims := Claims{
		UserID: userID,
		RegisteredClaims: jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(time.Now().Add(time.Duration(config.GlobalConfig.JWT.Expiration) * time.Millisecond)),
			IssuedAt:  jwt.NewNumericDate(time.Now()),
			Issuer:    config.GlobalConfig.JWT.Issuer,
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(config.GlobalConfig.JWT.Secret))
}

func ParseToken(tokenString string) (*Claims, error) {
	token, err := jwt.ParseWithClaims(tokenString, &Claims{}, func(token *jwt.Token) (interface{}, error) {
		return []byte(config.GlobalConfig.JWT.Secret), nil
	})

	if err != nil {
		return nil, err
	}

	if claims, ok := token.Claims.(*Claims); ok && token.Valid {
		return claims, nil
	}

	return nil, errors.New("invalid token")
}

func ValidateToken(tokenString string) bool {
	_, err := ParseToken(tokenString)
	return err == nil
}

func GetUserIDFromToken(tokenString string) (int, error) {
	claims, err := ParseToken(tokenString)
	if err != nil {
		return 0, err
	}
	return claims.UserID, nil
}
