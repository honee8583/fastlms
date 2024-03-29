package com.example.fastlms.course.service;

import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.model.CourseInput;
import com.example.fastlms.course.model.CourseParam;
import com.example.fastlms.course.model.ServiceResult;
import com.example.fastlms.course.model.TakeCourseInput;

import java.util.List;

public interface CourseService {
    /**
     * 강좌등록
     */
    boolean add(CourseInput courseInput);

    /**
     * 강좌 수정
     */
    boolean set(CourseInput courseInput);

    /**
     * 강좌목록
     */
    List<CourseDto> list(CourseParam parameter);

    /**
     * 강좌 상세정보
     */
    CourseDto getById(long id);

    /**
     * 강좌 내용 삭제
     */
    boolean del(String idList);

    /**
     * 프론트 쪽 강좌 목록
     */
    List<CourseDto> frontList(CourseParam courseParam);

    /**
     * 프론트 쪽 강좌 상세 정보
     */
    CourseDto frontDetail(long id);

    /**
     * 수강신청
     */
    ServiceResult req(TakeCourseInput takeCourseInput);

    /**
     * 전체 강좌 목록
     */
    List<CourseDto> listAll();
}
