package com.reddit.clone.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private Long id;
    private Long userId;

    @Size(max = 50, message = "Display name cannot exceed 50 characters")
    private String displayName;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    private String profileImageUrl;
    private String bannerImageUrl;

    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    @Size(max = 200, message = "Website cannot exceed 200 characters")
    private String website;

    private Instant updatedDate;
}
