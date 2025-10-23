package com.reddit.clone.repository;

import com.reddit.clone.entity.Post;
import com.reddit.clone.entity.User;
import com.reddit.clone.entity.Vote;
import com.reddit.clone.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByPostAndUser(Post post, User user);

    @Query("SELECT COUNT(v) FROM Vote v WHERE v.post = :post AND v.voteType = :voteType")
    Long countByPostAndVoteType(@Param("post") Post post, @Param("voteType") VoteType voteType);

    boolean existsByPostAndUser(Post post, User user);

    void deleteByPostAndUser(Post post, User user);
}
