package com.example.fastlms.course.mapper;

import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.model.CourseParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper // xml과 매핑
public interface CourseMapper {
    long selectListCount(CourseParam courseParam);
    List<CourseDto> selectList(CourseParam courseParam);
}
