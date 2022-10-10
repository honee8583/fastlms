package com.example.fastlms.admin.service;

import com.example.fastlms.admin.dto.CategoryDto;
import com.example.fastlms.admin.entity.Category;
import com.example.fastlms.admin.model.CategoryInput;

import java.util.List;

public interface CategoryService {

    /**
     * 카테고리 목록
     */
    List<CategoryDto> list();
    /**
     * 카테고리 신규 추가
     */
    boolean add(String categoryName);

    /**
     * 카테고리 수정
     */
    boolean update(CategoryInput categoryInput);

    /**
     * 카테고리 삭제
     */
    boolean del(long id);
}
