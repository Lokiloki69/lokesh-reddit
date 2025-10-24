package com.reddit.clone.repository;

import com.reddit.clone.entity.Community;
import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCommunity(Community community);

    Page<Post> findByCommunity(Community community, Pageable pageable);

    List<Post> findByUser(User user);

    Page<Post> findByUser(User user, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    Page<Post> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.voteCount DESC, p.createdDate DESC")
    Page<Post> findHotPosts(Pageable pageable);

    @Query("SELECT p FROM Post p ORDER BY p.createdDate DESC")
    Page<Post> findNewPosts(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.community = :community ORDER BY p.voteCount DESC")
    Page<Post> findTopPostsByCommunity(@Param("community") Community community, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:text% OR p.content LIKE %:text%")
    Page<Post> searchByTitleOrContent(@Param("text") String text, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);


}