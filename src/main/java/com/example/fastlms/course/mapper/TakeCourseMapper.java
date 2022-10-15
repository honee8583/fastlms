package com.example.fastlms.course.mapper;

import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.dto.TakeCourseDto;
import com.example.fastlms.course.model.CourseParam;
import com.example.fastlms.course.model.TakeCourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // xml과 매핑
public interface TakeCourseMapper {
    long selectListCount(TakeCourseParam takeCourseParam);
    List<TakeCourseDto> selectList(TakeCourseParam takeCourseParam);

    List<TakeCourseDto> selectListMyCourse(TakeCourseParam takeCourseParam);
}
