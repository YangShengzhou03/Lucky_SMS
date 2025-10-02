package com.yangshengzhou.lucky_sms.controller.Student;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/student")
public class StudentController {

    @PostMapping("/home")
    public HashMap<String, Object> studentHomeResult(@RequestParam String name){
        HashMap<String, Object> studentHomeResult = new HashMap<>();

        return studentHomeResult;
    }
}
