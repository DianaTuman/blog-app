package com.dianatuman.practicum.repository;

public interface CommentRepository {

    long addComment(long postId, String text);

    void editComment(long commentId, String text);

    void deleteComment(long commentId);
}
