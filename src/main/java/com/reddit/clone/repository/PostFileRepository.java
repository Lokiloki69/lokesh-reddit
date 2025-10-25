package com.reddit.clone.repository;

import com.reddit.clone.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    void deleteByIdIn(List<Long> ids);
}
