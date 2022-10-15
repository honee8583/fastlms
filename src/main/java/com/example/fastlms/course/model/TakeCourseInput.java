package com.example.fastlms.course.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TakeCourseInput {
    private Long courseId;
    private String userId;

    private long takeCourseId;  // 수강취소신청을 위한 아이디값
}
