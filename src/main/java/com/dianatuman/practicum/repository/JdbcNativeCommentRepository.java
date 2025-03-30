package com.dianatuman.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addComment(long postId, String text) {
        jdbcTemplate.update("insert into comments(post_id, text) values(?, ?)",
                postId, text);
    }

    @Override
    public void editComment(long commentId, String text) {
        jdbcTemplate.update("update comments " +
                "set text = ? " +
                "where id = ?", text, commentId);
    }

    @Override
    public void deleteComment(long commentId) {
        jdbcTemplate.update("delete from comments " +
                "where id = ?", commentId);
    }
}
