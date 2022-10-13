package com.example.fastlms.course.controller;

import com.example.fastlms.common.model.ResponseResult;
import com.example.fastlms.common.model.ResponseResultHeader;
import com.example.fastlms.course.model.ServiceResult;
import com.example.fastlms.course.model.TakeCourseInput;
import com.example.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ApiCourseController extends BaseController{

    private final CourseService courseService;

    @PostMapping("/api/course/req.api")
    public ResponseEntity<?> courseReq(Model model,
                           @RequestBody TakeCourseInput takeCourseInput,
                           Principal principal) {

        takeCourseInput.setUserId(principal.getName());

        ServiceResult result = courseService.req(takeCourseInput);

        if (!result.isResult()) {
            ResponseResult responseResult =
                    new ResponseResult(false, result.getMessage());

            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult =
                new ResponseResult(true);

        return ResponseEntity.ok().body(responseResult);
    }
}
