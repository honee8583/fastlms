package com.example.fastlms.course.controller;

import com.example.fastlms.admin.dto.MemberDto;
import com.example.fastlms.admin.service.CategoryService;
import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.model.CourseInput;
import com.example.fastlms.course.model.CourseParam;
import com.example.fastlms.course.service.CourseService;
import com.example.fastlms.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminCourseController extends BaseController{

    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/admin/course/list.do")
    public String list(Model model, CourseParam courseParam) {

        courseParam.init();
        List<CourseDto> courseList = courseService.list(courseParam);

        long totalCount = 0;
        if (!CollectionUtils.isEmpty(courseList)) {
            totalCount = courseList.get(0).getTotalCount();
        }
        String queryString = courseParam.getQueryString();
        String pagerHtml = super.getPagerHtml(totalCount,
                courseParam.getPageSize(),
                courseParam.getPageIndex(),
                queryString);

        model.addAttribute("list", courseList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/course/list";
    }

    @GetMapping(value = {"/admin/course/add.do", "/admin/course/edit.do"})
    public String add(Model model, HttpServletRequest request
    ,CourseInput courseInput) {

        // 카테고리 정보
        model.addAttribute("categoryList", categoryService.list());

        boolean editMode = request.getRequestURI().contains("/edit.do");
        CourseDto detail = new CourseDto();

        if (editMode) {
            long id = courseInput.getId();
            CourseDto existCourse = courseService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";
            }
            detail = existCourse;
        }

        model.addAttribute("editMode", editMode);
        model.addAttribute("detail", detail);

        return "admin/course/add";
    }

    @PostMapping(value = {"/admin/course/add.do", "/admin/course/edit.do"})
    public String addSubmit(Model model,
                            HttpServletRequest request,
                            CourseInput courseInput) {

        boolean editMode = request.getRequestURI().contains("/edit.do");

        if (editMode) {
            long id = courseInput.getId();
            CourseDto existCourse = courseService.getById(id);
            if (existCourse == null) {
                // error 처리
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";
            }

            boolean result = courseService.set(courseInput);
        } else {
            boolean result = courseService.add(courseInput);
        }


        return "redirect:/admin/course/list.do";
    }
}
