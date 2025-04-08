package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.configuration.TestDataSourceConfiguration;
import com.dianatuman.practicum.model.Comment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootTest(classes = TestDataSourceConfiguration.class)
public class JdbcNativeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("insert into posts(id, title, post_text) values (100, 'FIRST POST', 'FIRST TEXT')");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text) VALUES (100, 100, 'FIRST COMMENT')");
    }

    protected List<Comment> getAllComments() {
        return jdbcTemplate.query("select id, text from comments",
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text")));
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
    }
}
