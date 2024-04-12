package com.se309.soccernexus.util.recyclerviews;

import java.time.LocalDateTime;

public class Post {

    public String teamName;
    public String postContent;

    public LocalDateTime postTime;

    public Post(String teamName, String postContent, LocalDateTime postTime) {
        this.teamName = teamName;
        this.postContent = postContent;
        this.postTime = postTime;
    }
}
