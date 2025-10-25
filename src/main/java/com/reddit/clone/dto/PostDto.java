//package com.reddit.clone.dto;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.reddit.clone.entity.PostType;
//import jakarta.persistence.Transient;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Size;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.time.Instant;
//import java.util.List;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class PostDto {
//
//    private Long id;
//
//    @NotBlank(message = "Title is required")
//    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
//    private String title;
//
//    @Size(max = 10000, message = "Content cannot exceed 10000 characters")
//    private String content;
//
//    private String imageUrl;
//
//    private PostType postType;
//
//    private Integer voteCount;
//
//    private Instant createdDate;
//
//    @NotBlank(message = "Community name is required")
//    private String communityName;
//
//    private Long communityId;
//
//    private String username;
//
//    private Long userId;
//
//    private Integer commentCount;
//
//    private String voteStatus; // UPVOTE, DOWNVOTE, or null
////    private transient List<PostFileDto> files;
//
//}
//
package com.reddit.clone.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reddit.clone.entity.PostType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    @Size(max = 10000, message = "Content cannot exceed 10000 characters")
    private String content;

    private String imageUrl;

    private PostType postType;

    private Integer voteCount;

    private Instant createdDate;

    @NotBlank(message = "Community name is required")
    private String communityName;

    private Long communityId;

    private String username;

    private Long userId;

    private Integer commentCount;

    private String voteStatus;

    private String primaryMediaUrl;

    private String primaryMediaType;
}