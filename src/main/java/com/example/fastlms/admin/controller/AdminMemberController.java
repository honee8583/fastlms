package com.example.fastlms.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminMemberController {

    @GetMapping("/admin/member/list.do")
    public String list() {

        return "admin/member/list";
    }
}
