package com.example.fastlms.course.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CourseInput {
    private Long id;
    private Long categoryId;
    private String subject;
    private String keyword;
    private String summary;
    private String contents;
    private Long price;
    private Long salePrice;
    private String saleEndDtText;
}
