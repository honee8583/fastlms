package com.example.fastlms.course.model;

import com.example.fastlms.admin.model.CommonParam;
import lombok.*;

@Data
public class CourseParam extends CommonParam {
    private long categoryId;    // 강좌목록페이지에서 요청한 categoryId 정보
}
