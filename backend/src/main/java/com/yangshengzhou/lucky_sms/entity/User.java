package com.yangshengzhou.lucky_sms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class User {
    private Integer userId;              // 用户ID，自增
    private String username;             // 用户名
    private String passwordHash;         // 密码哈希
    private String email;                // 邮箱
    private String phone;                // 手机号
    private String gender;               // 性别，取值为'M'、'F'、'O'
    private LocalDate birthDate;         // 出生日期
    private String avatarUrl;            // 头像URL
    private String externalId;           // 外部ID
    private String status;               // 状态，默认为'ACTIVE'，可选'INACTIVE'
    private LocalDateTime lastLoginTime; // 最后登录时间
    private String lastLoginIp;          // 最后登录IP
    private LocalDateTime lastPasswordChangeTime; // 最后密码修改时间
    private LocalDateTime createdAt;     // 创建时间，默认当前时间
    private LocalDateTime updatedAt;     // 更新时间，默认当前时间

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public LocalDateTime getLastPasswordChangeTime() {
        return lastPasswordChangeTime;
    }

    public void setLastPasswordChangeTime(LocalDateTime lastPasswordChangeTime) {
        this.lastPasswordChangeTime = lastPasswordChangeTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
