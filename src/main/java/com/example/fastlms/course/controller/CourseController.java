package com.example.fastlms.course.controller;

import com.example.fastlms.admin.dto.CategoryDto;
import com.example.fastlms.admin.service.CategoryService;
import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.model.CourseInput;
import com.example.fastlms.course.model.CourseParam;
import com.example.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CourseController extends BaseController{

    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/course")
    public String course(Model model, CourseParam courseParam) {

        List<CourseDto> courseList = courseService.frontList(courseParam);
        model.addAttribute("courseList", courseList);

        int courseTotalCount = 0;
        List<CategoryDto> categoryList = categoryService.frontList(
                CategoryDto.builder().build());

        if (categoryList != null) {
            for (CategoryDto dto : categoryList) {
                courseTotalCount += dto.getCourseCount();
            }
        }


        model.addAttribute("categoryList", categoryList);
        model.addAttribute("courseTotalCount", courseTotalCount);

        return "course/index";
    }

    @GetMapping("/course/{id}")
    public String courseDetail(Model model, CourseParam courseParam) {

        CourseDto detail = courseService.frontDetail(courseParam.getId());
        model.addAttribute("detail", detail);

        return "course/detail";
    }
}
