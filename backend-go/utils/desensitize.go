package utils

import (
	"strings"
)

func DesensitizePhone(phone string) string {
	if phone == "" || len(phone) != 11 {
		return phone
	}
	return phone[:3] + "****" + phone[7:]
}

func DesensitizeEmail(email string) string {
	if email == "" {
		return email
	}
	parts := strings.Split(email, "@")
	if len(parts) != 2 {
		return email
	}
	username := parts[0]
	domain := parts[1]

	if len(username) <= 2 {
		return email
	}

	return username[:1] + "***" + username[len(username)-1:] + "@" + domain
}

func DesensitizeID(idCard string) string {
	if idCard == "" || len(idCard) != 18 {
		return idCard
	}
	return idCard[:6] + "********" + idCard[14:]
}
