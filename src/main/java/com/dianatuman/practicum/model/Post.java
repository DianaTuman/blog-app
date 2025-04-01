package com.dianatuman.practicum.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Post {

    private long id;
    private String title;
    private String text;
    private int likesCount;
    private List<Comment> comments;
    private int commentsSize;
    private String tags;
    private byte[] image;

    public Post(long id, String title, String text, int likesCount, String tags, int commentsSize) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.likesCount = likesCount;
        this.tags = tags;
        this.commentsSize = commentsSize;
        this.comments = new ArrayList<>();
    }

    public Post(String title, String text, byte[] image, String tags) {
        this.title = title;
        this.text = text;
        this.tags = Objects.requireNonNullElse(tags, "");
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", likesCount=" + likesCount +
                ", tags='" + tags + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id && likesCount == post.likesCount && Objects.equals(title, post.title)
                && Objects.equals(text, post.text) && Objects.equals(tags, post.tags) && Objects.deepEquals(image, post.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, likesCount, comments, tags, Arrays.hashCode(image));
    }
}
