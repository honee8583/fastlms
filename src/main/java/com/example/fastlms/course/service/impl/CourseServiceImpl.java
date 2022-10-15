package com.example.fastlms.course.service.impl;

import com.example.fastlms.course.dto.CourseDto;
import com.example.fastlms.course.entity.Course;
import com.example.fastlms.course.entity.TakeCourse;
import com.example.fastlms.course.mapper.CourseMapper;
import com.example.fastlms.course.model.CourseInput;
import com.example.fastlms.course.model.CourseParam;
import com.example.fastlms.course.model.ServiceResult;
import com.example.fastlms.course.model.TakeCourseInput;
import com.example.fastlms.course.repository.CourseRepository;
import com.example.fastlms.course.repository.TakeCourseRepository;
import com.example.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public
class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TakeCourseRepository takeCourseRepository;
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

    @Override
    public List<CourseDto> frontList(CourseParam courseParam) {
        // 전체를 요청한경우
        if (courseParam.getCategoryId() < 1) {
            List<Course> courseList = courseRepository.findAll();
            return CourseDto.of(courseList);
        }

//        return courseRepository.findByCategoryId(courseParam.getCategoryId()).map(CourseDto::of).orElse(null);

        Optional<List<Course>> optionalCourseList =
                courseRepository.findByCategoryId(courseParam.getCategoryId());

        if (optionalCourseList.isPresent()) {
            return CourseDto.of(optionalCourseList.get());
        }

        return null;
    }

    @Override
    public CourseDto frontDetail(long id) {

        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            return CourseDto.of(optionalCourse.get());
        }
        return null;
    }

    @Override
    public ServiceResult req(TakeCourseInput takeCourseInput) {

        ServiceResult result = new ServiceResult();

        Optional<Course> optionalCourse = courseRepository.findById(takeCourseInput.getCourseId());
        if (!optionalCourse.isPresent()) {
            result.setResult(false);
            result.setMessage("강좌정보가 존재하지 않습니다.");
            return result;
        }

        Course course = optionalCourse.get();

        // 이미 신청정보가 있는지 확인 필요
        String[] statusList = {TakeCourse.STATUS_REQ, TakeCourse.STATUS_COMPLETE};
        long count = takeCourseRepository.countByCourseIdAndUserIdAndStatusIn(
                course.getId(), takeCourseInput.getUserId(), Arrays.asList(statusList)
        );

        if (count > 0) {
            result.setResult(false);
            result.setMessage("이미 신청한 강좌정보가 존재합니다.");
            return result;
        }

        TakeCourse takeCourse = TakeCourse.builder()
                .courseId(course.getId())
                .userId(takeCourseInput.getUserId())
                .payPrice(course.getSalePrice())
                .status(TakeCourse.STATUS_REQ)
                .regDt(LocalDateTime.now())
                .build();
        takeCourseRepository.save(takeCourse);

        result.setResult(true);
        result.setMessage("");

        return result;
    }

    @Override
    public List<CourseDto> listAll() {
        List<Course> courseList = courseRepository.findAll();
        return CourseDto.of(courseList);
    }
}
