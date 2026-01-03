package com.yangshengzhou.lucky_sms.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("announcement_types")
public class AnnouncementType {
    @TableId(type = IdType.AUTO)
    private Integer announcementTypeId;
    private String typeName;
    private String typeCode;
    private String description;
    private java.time.LocalDateTime createTime;

    public Integer getAnnouncementTypeId() {
        return announcementTypeId;
    }

    public void setAnnouncementTypeId(Integer announcementTypeId) {
        this.announcementTypeId = announcementTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
