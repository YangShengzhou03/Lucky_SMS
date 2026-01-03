package utils

import (
	"testing"
)

func TestHashPassword(t *testing.T) {
	password := "test123"
	hash, err := HashPassword(password)
	if err != nil {
		t.Errorf("HashPassword() error = %v", err)
		return
	}

	if hash == "" {
		t.Error("HashPassword() returned empty hash")
	}

	if hash == password {
		t.Error("HashPassword() returned plain password")
	}
}

func TestCheckPassword(t *testing.T) {
	password := "test123"
	hash, _ := HashPassword(password)

	tests := []struct {
		name     string
		password string
		hash     string
		want     bool
	}{
		{
			name:     "Correct password",
			password: password,
			hash:     hash,
			want:     true,
		},
		{
			name:     "Wrong password",
			password: "wrong",
			hash:     hash,
			want:     false,
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := CheckPassword(tt.password, tt.hash); got != tt.want {
				t.Errorf("CheckPassword() = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestDesensitizePhone(t *testing.T) {
	tests := []struct {
		name  string
		phone string
		want  string
	}{
		{
			name:  "Valid phone",
			phone: "13812345678",
			want:  "138****5678",
		},
		{
			name:  "Short phone",
			phone: "123456",
			want:  "123456",
		},
		{
			name:  "Empty phone",
			phone: "",
			want:  "",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := DesensitizePhone(tt.phone); got != tt.want {
				t.Errorf("DesensitizePhone() = %v, want %v", got, tt.want)
			}
		})
	}
}

func TestDesensitizeEmail(t *testing.T) {
	tests := []struct {
		name  string
		email string
		want  string
	}{
		{
			name:  "Valid email",
			email: "test@example.com",
			want:  "t***t@example.com",
		},
		{
			name:  "Short email",
			email: "ab@example.com",
			want:  "ab@example.com",
		},
		{
			name:  "Empty email",
			email: "",
			want:  "",
		},
	}

	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := DesensitizeEmail(tt.email); got != tt.want {
				t.Errorf("DesensitizeEmail() = %v, want %v", got, tt.want)
			}
		})
	}
}
