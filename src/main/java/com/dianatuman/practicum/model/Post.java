package com.dianatuman.practicum.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class Post {

    private Long id;
    private String title;
    private String text;
    private int likesCount;
    private List<Comment> comments;
    private List<String> tags;
    private MultipartFile image;

    public Post(Long id, String title, String text, int likesCount, List<Comment> comments, List<String> tags) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.likesCount = likesCount;
        this.comments = comments;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTextPreview() {
        return text.substring(0, 10);
    }

    public int getLikesCount() {
        return likesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<String> getTags() {
        return tags;
    }
}
