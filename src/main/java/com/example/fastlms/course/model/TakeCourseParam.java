package com.example.fastlms.course.model;

import com.example.fastlms.admin.model.CommonParam;
import lombok.Data;

@Data
public class TakeCourseParam extends CommonParam {
    private long id;
    private String status;

    private String userId;

    private long searchCourseId;
}
