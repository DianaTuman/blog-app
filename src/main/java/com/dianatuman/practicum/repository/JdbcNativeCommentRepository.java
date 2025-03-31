package com.dianatuman.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long addComment(long postId, String text) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("insert into comments(post_id, text) values(?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, postId);
            ps.setString(2, text);
            return ps;
        }, keyHolder);
        if (keyHolder.getKeys() == null) {
            return 0;
        } else {
            return (long) keyHolder.getKeys().get("id");
        }
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
