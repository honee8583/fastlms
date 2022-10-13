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
}
