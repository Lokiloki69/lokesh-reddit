package com.reddit.clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user_profiles")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String displayName;

    @Column(length = 500)
    private String bio;

    private String profileImageUrl;
    private String bannerImageUrl;

    private String location;
    private String website;

    private Instant updatedDate;

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        updatedDate = Instant.now();
    }
}
