package com.reddit.clone.util;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component("timeAgoUtil")
public class TimeAgoUtil {

    public String toTimeAgo(Instant createdDate) {
        if (createdDate == null) return "";

        Duration duration = Duration.between(createdDate, Instant.now());

        long seconds = duration.getSeconds();

        if (seconds < 60) return "just now";
        if (seconds < 3600) return (seconds / 60) + " minutes ago";
        if (seconds < 86400) return (seconds / 3600) + " hours ago";
        if (seconds < 604800) return (seconds / 86400) + " days ago";
        if (seconds < 2419200) return (seconds / 604800) + " weeks ago";
        return (seconds / 2419200) + " months ago";
    }
}
