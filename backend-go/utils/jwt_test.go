package utils

import (
	"testing"
)

func TestGenerateToken(t *testing.T) {
	userID := 123
	token, err := GenerateToken(userID)
	if err != nil {
		t.Errorf("GenerateToken() error = %v", err)
		return
	}

	if token == "" {
		t.Error("GenerateToken() returned empty token")
	}
}

func TestParseToken(t *testing.T) {
	userID := 123
	token, _ := GenerateToken(userID)

	claims, err := ParseToken(token)
	if err != nil {
		t.Errorf("ParseToken() error = %v", err)
		return
	}

	if claims.UserID != userID {
		t.Errorf("ParseToken() userID = %v, want %v", claims.UserID, userID)
	}
}

func TestValidateToken(t *testing.T) {
	userID := 123
	token, _ := GenerateToken(userID)

	if !ValidateToken(token) {
		t.Error("ValidateToken() returned false for valid token")
	}

	invalidToken := "invalid.token.here"
	if ValidateToken(invalidToken) {
		t.Error("ValidateToken() returned true for invalid token")
	}
}

func TestGetUserIDFromToken(t *testing.T) {
	userID := 123
	token, _ := GenerateToken(userID)

	gotUserID, err := GetUserIDFromToken(token)
	if err != nil {
		t.Errorf("GetUserIDFromToken() error = %v", err)
		return
	}

	if gotUserID != userID {
		t.Errorf("GetUserIDFromToken() = %v, want %v", gotUserID, userID)
	}

	_, err = GetUserIDFromToken("invalid.token")
	if err == nil {
		t.Error("GetUserIDFromToken() expected error for invalid token")
	}
}
