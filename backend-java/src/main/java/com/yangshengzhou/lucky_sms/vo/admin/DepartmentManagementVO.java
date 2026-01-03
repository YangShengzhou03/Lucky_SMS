package com.yangshengzhou.lucky_sms.vo.admin;

import java.util.List;

public class DepartmentManagementVO {
    private List<DepartmentInfo> records;
    private Long total;
    private Integer size;
    private Integer current;
    private Integer pages;

    public List<DepartmentInfo> getRecords() {
        return records;
    }

    public void setRecords(List<DepartmentInfo> records) {
        this.records = records;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public static class DepartmentInfo {
        private Integer id;
        private String deptCode;
        private String deptName;
        private String deanName;
        private String phone;
        private String email;
        private Integer teacherCount;
        private Integer studentCount;
        private String status;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getDeanName() {
            return deanName;
        }

        public void setDeanName(String deanName) {
            this.deanName = deanName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getTeacherCount() {
            return teacherCount;
        }

        public void setTeacherCount(Integer teacherCount) {
            this.teacherCount = teacherCount;
        }

        public Integer getStudentCount() {
            return studentCount;
        }

        public void setStudentCount(Integer studentCount) {
            this.studentCount = studentCount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}