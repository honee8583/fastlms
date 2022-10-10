package com.example.fastlms.course.service;

import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.model.CourseInput;
import com.example.fastlms.course.model.CourseParam;

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
}
