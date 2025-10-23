package com.reddit.clone.repository;

import com.reddit.clone.entity.Comment;
import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findByUser(User user);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post = :post")
    Long countByPost(@Param("post") Post post);

    @Query("SELECT c FROM Comment c WHERE c.post = :post ORDER BY c.createdDate ASC")
    List<Comment> findByPostOrderByCreatedDateAsc(@Param("post") Post post);
}
