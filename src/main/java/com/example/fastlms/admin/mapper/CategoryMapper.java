package com.example.fastlms.admin.mapper;

import com.example.fastlms.admin.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryDto> select(CategoryDto categoryDto);
}
