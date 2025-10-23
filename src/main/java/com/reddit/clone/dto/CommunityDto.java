package com.reddit.clone.dto;

import com.reddit.clone.entity.CommunityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityDto {

    private Long id;

    @NotBlank(message = "Community name is required")
    @Size(min = 3, max = 20, message = "Community name must be between 3 and 20 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private CommunityType type;

    private Instant createdDate;

    private String creatorUsername;

    private Integer postCount;
}
