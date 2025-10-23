package com.reddit.clone.repository;

import com.reddit.clone.entity.Community;
import com.reddit.clone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    Optional<Community> findByName(String name);

    boolean existsByName(String name);

    List<Community> findByCreator(User creator);

    @Query("SELECT c FROM Community c WHERE c.name LIKE %:keyword% OR c.description LIKE %:keyword%")
    Page<Community> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT c FROM Community c ORDER BY SIZE(c.posts) DESC")
    Page<Community> findPopularCommunities(Pageable pageable);
}