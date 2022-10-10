package com.example.fastlms.admin.service.impl;

import com.example.fastlms.admin.dto.CategoryDto;
import com.example.fastlms.admin.entity.Category;
import com.example.fastlms.admin.model.CategoryInput;
import com.example.fastlms.admin.repository.CategoryRepository;
import com.example.fastlms.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private Sort getSortBySortValueDesc() {

        return Sort.by(Sort.Direction.DESC, "sortValue");
    }

    @Override
    public List<CategoryDto> list() {
        List<Category> optionalCategoryList
                = categoryRepository.findAll(getSortBySortValueDesc());

        return CategoryDto.of(optionalCategoryList);
    }

    @Override
    public boolean add(String categoryName) {

        // 카테고리명이 중복인지 체크

        Category category = Category.builder()
                .categoryName(categoryName)
                .usingYn(true)
                .sortValue(0)
                .build();

        categoryRepository.save(category);

        return true;
    }

    @Override
    public boolean update(CategoryInput categoryInput) {
        Optional<Category> optionalCategory =
                categoryRepository.findById(categoryInput.getId());

        if (optionalCategory.isPresent()) {

            Category category = optionalCategory.get();
             category.setCategoryName(categoryInput.getCategoryName());
             category.setSortValue(categoryInput.getSortValue());
             category.setUsingYn(categoryInput.isUsingYn());
             categoryRepository.save(category);
        }

        return true;
    }

    @Override
    public boolean del(long id) {

        categoryRepository.deleteById(id);

        return true;
    }
}
