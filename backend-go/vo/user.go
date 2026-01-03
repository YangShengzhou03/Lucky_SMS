package vo

type LoginVO struct {
	UID   int    `json:"uid"`
	Phone string `json:"phone"`
	Role  string `json:"role"`
	Token string `json:"token"`
}

type RegisterVO struct {
	UID   int    `json:"uid"`
	Phone string `json:"phone"`
}

type ResetPasswordVO struct {
	Success bool `json:"success"`
}
