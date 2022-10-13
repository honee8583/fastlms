package com.example.fastlms.course.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
public class TakeCourse implements TakeCourseCode{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;
    private String userId;

    private long payPrice;  // 결제금액
    private String status;  // 상태(수강신청, 결제완료, 수강취소)

    private LocalDateTime regDt;    // 신청일
}
