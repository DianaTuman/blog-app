package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.model.Comment;
import com.dianatuman.practicum.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(
                "select id, title, post_text, likes, tags, comments_size from posts " +
                        "left join (select post_id, count(id) as comments_size from comments group by post_id) " +
                        "on posts.id = post_id",
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("post_text"),
                        rs.getInt("likes"),
                        rs.getString("tags"),
                        rs.getInt("comments_size")
                ));
    }

    @Override
    public long addPost(Post newPost) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("insert into posts(title, post_text, tags, image) values(?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newPost.getTitle());
            ps.setString(2, newPost.getText());
            ps.setString(3, newPost.getTagsAsText());
            ps.setBytes(4, newPost.getImage());
            return ps;
        }, keyHolder);
        if (keyHolder.getKeys() == null) {
            return 0;
        } else {
            return (long) keyHolder.getKeys().get("id");
        }
    }

    @Override
    public Post getPost(long id) {
        List<Post> query = jdbcTemplate.query(
                "select id, title, post_text, likes, tags, comments_size from posts " +
                        "left join (select post_id, sum(id) as comments_size from comments group by post_id) " +
                        "on posts.id = post_id " +
                        "where id = " + id,
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("post_text"),
                        rs.getInt("likes"),
                        rs.getString("tags"),
                        rs.getInt("comments_size")
                ));
        if (query.isEmpty()) {
            return null;
        } else {
            Post post = query.getFirst();
            post.setComments(jdbcTemplate.query(
                    "select id, text from comments " +
                            "where post_id = " + id,
                    (rs, rowNum) -> new Comment(
                            rs.getLong("id"),
                            rs.getString("text"))));
            return post;
        }
    }

    @Override
    public void editPost(long id, Post updatedPost) {
        //TODO refactor
        if (updatedPost.getImage().length > 0) {
            jdbcTemplate.update("update posts " +
                            "set title = ?, post_text = ?, tags = ?, image = ? " +
                            "where id = ?",
                    updatedPost.getTitle(), updatedPost.getText(), updatedPost.getTagsAsText(), updatedPost.getImage(),
                    id);
        } else {
            jdbcTemplate.update("update posts " +
                            "set title = ?, post_text = ?, tags = ? " +
                            "where id = ?",
                    updatedPost.getTitle(), updatedPost.getText(), updatedPost.getTagsAsText(),
                    id);
        }
    }

    @Override
    public void deletePost(long id) {
        jdbcTemplate.update("delete from comments " +
                "where post_id = ?", id);
        jdbcTemplate.update("delete from posts " +
                "where id = ?", id);
    }

    @Override
    public void updateLikes(long id, int likes) {
        jdbcTemplate.update("update posts " +
                "set likes = likes + (?)" +
                "where id = ?", likes, id);
    }

    @Override
    public byte[] getImage(long id) {
        return jdbcTemplate.query(
                String.format("select image from posts " +
                        "where id ='%s'", id),
                (rs, rowNum) -> rs.getBytes("image")).getFirst();
    }

    @Override
    public List<Post> findPostsByTag(String search) {
        //TODO
        return List.of();
    }
}