package com.example.fastlms.course.service.impl;

import com.example.fastlms.admin.dto.MemberDto;
import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.entity.Course;
import com.example.fastlms.course.mapper.CourseMapper;
import com.example.fastlms.course.model.CourseInput;
import com.example.fastlms.course.model.CourseParam;
import com.example.fastlms.course.repository.CourseRepository;
import com.example.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.swing.plaf.ColorUIResource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public boolean add(CourseInput courseInput) {

        Course course = Course.builder()
                .categoryId(courseInput.getCategoryId())
                .subject(courseInput.getSubject())
                .regDt(LocalDateTime.now())
                .build();
        courseRepository.save(course);

        return true;
    }

    @Override
    public boolean set(CourseInput courseInput) {

        Optional<Course> optionalCourse =
                courseRepository.findById(courseInput.getId());

        if (!optionalCourse.isPresent()) {
            // 수정할 데이터가 없음
            return false;
        }

        Course course = optionalCourse.get();
        course.setCategoryId(courseInput.getCategoryId());
        course.setSubject(courseInput.getSubject());
        course.setUpDt(LocalDateTime.now());

        courseRepository.save(course);

        return true;
    }

    @Override
    public List<CourseDto> list(CourseParam courseParam) {
        long totalCount = courseMapper.selectListCount(courseParam);
        List<CourseDto> list = courseMapper.selectList(courseParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (CourseDto x: list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - courseParam.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public CourseDto getById(long id) {
        return courseRepository.findById(id)
                .map(CourseDto::of).orElse(null);
    }

}
