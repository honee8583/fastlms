package com.example.fastlms.admin.controller;

import com.example.fastlms.admin.dto.MemberDto;
import com.example.fastlms.admin.model.MemberParam;
import com.example.fastlms.member.service.MemberService;
import com.example.fastlms.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping("/admin/member/list.do")
    public String list(Model model, MemberParam parameter) {

        parameter.init();

        List<MemberDto> memberList = memberService.list(parameter);

        long totalCount = 0;
        if (memberList != null && memberList.size() > 0) {
            totalCount = memberList.get(0).getTotalCount();
        }
        String queryString = parameter.getQueryString();

        // pageIndex : 현재 페이지 번호
        PageUtil pageUtil =
                new PageUtil(totalCount,
                        parameter.getPageSize(),
                        parameter.getPageIndex(),
                        queryString);

        model.addAttribute("list", memberList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pageUtil.pager());  // pager(String)

        return "admin/member/list";
    }
}
