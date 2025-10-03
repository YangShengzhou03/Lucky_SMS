package com.yangshengzhou.lucky_sms.vo.student;

import java.util.HashMap;

public class StatusVO {
    /**
     *
     basicInfo: {
     id: "20220001",
     name: "张三",
     gender: "男",
     birthDate: "2003-05-15",
     idCard: "110101200305156789",
     nation: "汉族",
     politicalStatus: "共青团员",
     enrollmentDate: "2022-09-01",
     educationLevel: "本科",
     major: "计算机科学与技术",
     class: "计科2201班",
     department: "信息工程学院",
     advisor: "李教授",
     phone: "13800138000",
     email: "zhangsan@example.com",
     address: "北京市海淀区XX路XX号"
     },
     academicStatus: {
     status: "normal",
     gpa: "3.65",
     totalCredits: 140,
     completedCredits: 68,
     failedCourses: 2
     },
     // 添加历史学业表现数据，用于趋势图
     academicHistory: [
     { semester: "大一上", gpa: "3.2", attendanceRate: 92 },
     { semester: "大一下", gpa: "3.35", attendanceRate: 94 }
     ]
     }
     */

    private HashMap<String,String> basicInfo;

}