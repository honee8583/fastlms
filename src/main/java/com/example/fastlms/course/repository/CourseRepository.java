package com.example.fastlms.course.repository;

import com.example.fastlms.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<List<Course>> findByCategoryId(long categoryId);
}
