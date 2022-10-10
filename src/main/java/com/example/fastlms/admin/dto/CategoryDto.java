package com.example.fastlms.admin.dto;

import com.example.fastlms.admin.entity.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CategoryDto {
    private Long id;
    private String categoryName;
    private int sortValue;
    private boolean usingYn;

    public static CategoryDto of(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .sortValue(category.getSortValue())
                .usingYn(category.isUsingYn())
                .build();
    }

    public static List<CategoryDto> of (List<Category> categoryList) {

        if (categoryList != null) {
            List<CategoryDto> categoryDtoList = new ArrayList<>();
            for (Category category : categoryList) {
                categoryDtoList.add(of(category));
            }
            return categoryDtoList;
        }

        return null;
    }
}
