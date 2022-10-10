package com.example.fastlms.course.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long categoryId;

    private String imagePath;

    private String keyword;

    private String subject;

    @Column(length = 1000)
    private String summary;

    @Lob    // Large Object의 줄임말
    private String contents;

    private long price;

    private long salePrice;

    private LocalDateTime saleEndDt;

    private LocalDateTime regDt;    // 등록일
    private LocalDateTime upDt;     // 수정일
}
