package com.example.fastlms.course.service;

import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.dto.TakeCourseDto;
import com.example.fastlms.course.model.ServiceResult;
import com.example.fastlms.course.model.TakeCourseParam;

import java.util.List;

public interface TakeCourseService {
    /**
     * 수강 목록
     */
    List<TakeCourseDto> list(TakeCourseParam takeCourseParam);

    /**
     * 수강 상세 정보
     */
    TakeCourseDto detail(long id);

    /**
     * 회원의 수강상태 변경
     */
    ServiceResult updateStatus(long id, String status);

    /**
     * 내 수강내역
     */
    List<TakeCourseDto> myCourse(String userId);

    /**
     * 수강신청 취소 처리
     */
    ServiceResult cancel(long id);
}
