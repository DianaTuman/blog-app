package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.configuration.DataSourceConfiguration;
import com.dianatuman.practicum.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({DataSourceConfiguration.class, JdbcNativeCommentRepository.class})
@TestPropertySource("classpath:application.properties")
public class JdbcNativeCommentRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text) VALUES (1, 1, 'TESTCOMMENT1')");
    }

    @Test
    void add_shouldSaveCommentInDatabase() {
        long id = commentRepository.addComment(1, "TESTCOMMENT_ADD");
        assertNotEquals(0, id);

        var comments = getAllComments();
        Comment addedComment = comments.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        assertNotNull(addedComment);
        assertEquals("TESTCOMMENT_ADD", addedComment.getText());
    }

    @Test
    void edit_shouldUpdateCommentInDatabase() {
        commentRepository.editComment(1, "EDITED_TEXT");

        var comments = getAllComments();
        Comment editedComment = comments.stream()
                .filter(c -> c.getId() == 1)
                .findFirst()
                .orElse(null);

        assertNotNull(editedComment);
        assertEquals("EDITED_TEXT", editedComment.getText());
    }

    @Test
    void delete_shouldDeleteCommentInDatabase() {
        commentRepository.deleteComment(1);

        var comments = getAllComments();
        Comment deletedComment = comments.stream()
                .filter(c -> c.getId() == 1)
                .findFirst()
                .orElse(null);

        assertNull(deletedComment);
    }

    private List<Comment> getAllComments() {
        return jdbcTemplate.query("select id, text from comments",
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getString("text")));
    }

}
