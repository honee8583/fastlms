package com.example.fastlms.main.controller;

import com.example.fastlms.component.MailComponents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController { // 논리적 주소와 물리적 주소를 매핑

    private final MailComponents mailComponents;

    // return 되는 문자열은 파일명이 되기로 약속되어 있음.
    @RequestMapping("/")
    public String index() {

//        String mail = "honee85832@gmail.com";
//        String subject = "안녕하세요.";
//        String text = "<p>안녕하세요</p>" +
//                    "<p>반갑습니다</p>";
//
//        mailComponents.sendMail(mail, subject, text);

        return "index";
    }
}
