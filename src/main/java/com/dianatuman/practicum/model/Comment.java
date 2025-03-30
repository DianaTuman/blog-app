package com.dianatuman.practicum.model;

public class Comment {

    private final long id;
    private String text;

    public Comment(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
