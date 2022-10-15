package com.example.fastlms.course.dto;

import com.example.fastlms.course.entity.TakeCourse;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TakeCourseDto {
    private Long id;
    private Long courseId;
    private String userId;
    private long payPrice;  // 결제금액
    private String status;  // 상태(수강신청, 결제완료, 수강취소)
    private LocalDateTime regDt;    // 신청일

    // join column
    private String userName;
    private String phone;
    private String subject;

    // 추가 칼럼
    private long totalCount;    // 데이터 총 개수
    private long seq;           // 페이지내 번호

    public String getRegDtText() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return regDt != null ? regDt.format(formatter) : "";
    }

    public static TakeCourseDto of(TakeCourse takeCourse) {
        return TakeCourseDto.builder()
                .id(takeCourse.getId())
                .courseId(takeCourse.getCourseId())
                .userId(takeCourse.getUserId())
                .payPrice(takeCourse.getPayPrice())
                .status(takeCourse.getStatus())
                .regDt(takeCourse.getRegDt())
                .build();
    }
}
