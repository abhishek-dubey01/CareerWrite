package com.careerwrite.repository;

import com.careerwrite.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByRecruiterId(Long recruiterId);

    // Simple keyword + location + category search.
    // If a parameter is null/empty, that filter is skipped.
    @Query("SELECT j FROM Job j WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "  LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "  LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:location IS NULL OR :location = '' OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:category IS NULL OR :category = '' OR LOWER(j.category) LIKE LOWER(CONCAT('%', :category, '%')))")
    List<Job> searchJobs(@Param("keyword") String keyword,
                          @Param("location") String location,
                          @Param("category") String category);
}
