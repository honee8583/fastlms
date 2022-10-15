package com.example.fastlms.member.controller;

import com.example.fastlms.common.model.ResponseResult;
import com.example.fastlms.course.dto.TakeCourseDto;
import com.example.fastlms.course.model.ServiceResult;
import com.example.fastlms.course.model.TakeCourseInput;
import com.example.fastlms.course.service.TakeCourseService;
import com.example.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiMemberController {

    private final MemberService memberService;
    private final TakeCourseService takeCourseService;

    @PostMapping("/ap/member/course/cancel.api")
    public ResponseEntity<?> cancelCourse(Model model,
              Principal principal,
              @RequestBody TakeCourseInput takeCourseInput) {

        String userId = principal.getName();

        // 내 수강친청정보 불러오기
        TakeCourseDto detail = takeCourseService.detail(takeCourseInput.getCourseId());
        if (detail == null) {
            ResponseResult responseResult = new ResponseResult(false, "수강신청정보가 존재하지 않습니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        // 불러온 수강신청의 신청인이 자신이 맞는지 체크
        if (userId == null || !userId.equals(detail.getUserId())) {
            // 내 수강신청 정보가 아닌경우
            ResponseResult responseResult = new ResponseResult(false, "본인의 수강 신청 정보만 취소할 수 있습니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        // 자신이 맞는경우 취소진행
        ServiceResult cancelResult = takeCourseService.cancel(takeCourseInput.getCourseId());
        if (!cancelResult.isResult()) {
            ResponseResult responseResult = new ResponseResult(false, cancelResult.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        return ResponseEntity.ok().body(new ResponseResult(true));
    }
}
