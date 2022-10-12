package com.example.fastlms.course.service.impl;

import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.entity.Course;
import com.example.fastlms.course.mapper.CourseMapper;
import com.example.fastlms.course.model.CourseInput;
import com.example.fastlms.course.model.CourseParam;
import com.example.fastlms.course.repository.CourseRepository;
import com.example.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private LocalDate getLocalDate(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public boolean add(CourseInput courseInput) {

        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText());

        Course course = Course.builder()
                .categoryId(courseInput.getCategoryId())
                .subject(courseInput.getSubject())
                .keyword(courseInput.getKeyword())
                .summary(courseInput.getSummary())
                .contents(courseInput.getContents())
                .price(courseInput.getPrice())
                .salePrice(courseInput.getSalePrice())
                .saleEndDt(saleEndDt)
                .regDt(LocalDateTime.now())
                .build();
        courseRepository.save(course);

        return true;
    }

    @Override
    public boolean set(CourseInput courseInput) {

        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText());

        Optional<Course> optionalCourse =
                courseRepository.findById(courseInput.getId());

        if (!optionalCourse.isPresent()) {
            // 수정할 데이터가 없음
            return false;
        }

        Course course = optionalCourse.get();
        course.setCategoryId(courseInput.getCategoryId());
        course.setSubject(courseInput.getSubject());
        course.setKeyword(courseInput.getKeyword());
        course.setSummary(courseInput.getSummary());
        course.setContents(courseInput.getContents());
        course.setPrice(courseInput.getPrice());
        course.setSalePrice(courseInput.getSalePrice());
        course.setSaleEndDt(saleEndDt);
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
            for (CourseDto x : list) {
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

    @Override
    public boolean del(String idList) {

        if (idList != null && idList.length() > 0) {
            String[] ids = idList.split(",");

            for (String x : ids) {
                long id = 0L;
                try {
                    id = Long.parseLong(x);
                } catch (Exception e) {

                }

                if (id > 0) {
                    courseRepository.deleteById(id);
                }
            }
        }

        return true;
    }
}
