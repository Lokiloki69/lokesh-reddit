package com.reddit.clone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostFileDto {
    private String fileUrl;
    private String fileName;
    private String fileType;
}
