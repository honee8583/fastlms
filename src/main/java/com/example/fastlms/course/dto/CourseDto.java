package com.example.fastlms.course.dto;

import com.example.fastlms.course.entity.Course;
import lombok.*;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CourseDto {
    private Long id;
    private Long categoryId;
    private String imagePath;
    private String keyword;
    private String subject;
    private String summary;
    private String contents;
    private long price;
    private long salePrice;
    private LocalDate saleEndDt;
    private LocalDateTime regDt;
    private LocalDateTime upDt;

    // 추가칼럼
    private long totalCount;    // 데이터 총 개수
    private long seq;           // 페이지내 번호

    public static CourseDto of(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .categoryId(course.getCategoryId())
                .imagePath(course.getImagePath())
                .keyword(course.getKeyword())
                .subject(course.getSubject())
                .summary(course.getSummary())
                .contents(course.getContents())
                .price(course.getPrice())
                .salePrice(course.getSalePrice())
                .saleEndDt(course.getSaleEndDt())
                .regDt(course.getRegDt())
                .upDt(course.getUpDt())
                .build();
    }

    public static List<CourseDto> of(List<Course> courseList) {
        if (courseList == null) {
            return null;
        }

        List<CourseDto> courseDtoList = new ArrayList<>();
        for (Course x: courseList) {
            courseDtoList.add(CourseDto.of(x));
        }

        return courseDtoList;
    }
}
