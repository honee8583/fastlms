package com.example.fastlms.course.repository;

import com.example.fastlms.course.entity.TakeCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TakeCourseRepository extends JpaRepository<TakeCourse, Long> {
    long countByCourseIdAndUserIdAndStatusIn(long courseId, String userId, Collection<String> statusList);
}
