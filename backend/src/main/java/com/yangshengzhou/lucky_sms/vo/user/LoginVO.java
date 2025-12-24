package com.yangshengzhou.lucky_sms.vo.user;

public class LoginVO {
    private Integer uid;
    private String username;
    private String phone;
    private String role;
    private String token;

    public void setUserId(Integer userId) {
        this.uid = userId;
    }

    public void setRoleName(String roleName) {
        this.role = roleName;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return uid;
    }

    public String getRoleName() {
        return role;
    }

    public String getStudentId() {
        return uid != null ? uid.toString() : null;
    }
}