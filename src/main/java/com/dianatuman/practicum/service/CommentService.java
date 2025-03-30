package com.dianatuman.practicum.service;

import com.dianatuman.practicum.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(long postId, String text) {
        commentRepository.addComment(postId, text);
    }

    public void editComment(long commentId, String text) {
        commentRepository.editComment(commentId, text);
    }

    public void deleteComment(long commentId) {
        commentRepository.deleteComment(commentId);
    }
}
