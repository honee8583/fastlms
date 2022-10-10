package com.example.fastlms.admin.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryInput {
    private long id;
    private String categoryName;
    private int sortValue;
    private boolean usingYn;
}
