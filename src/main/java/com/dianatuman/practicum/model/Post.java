package com.dianatuman.practicum.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class Post {

    private final long id;
    private String title;
    private String text;
    private final int likesCount;
    private List<Comment> comments;
    private final int commentsSize;
    private String tags;
    private MultipartFile image;

    public Post(long id, String title, String text, int likesCount, String tags, int commentsSize) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.likesCount = likesCount;
        this.tags = tags;
        this.commentsSize = commentsSize;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextPreview() {
        return getTextParts()[0];
    }

    public String[] getTextParts() {
        return text.split("\n");
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getCommentsSize() {
        if (comments != null) {
            return comments.size();
        } else {
            return commentsSize;
        }
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String[] getTags() {
        return tags.split(" ");
    }

    public String getTagsAsText() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public MultipartFile getImage() {
        return image;
    }

    public byte[] getImageBytes() {
        try {
            return image.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
