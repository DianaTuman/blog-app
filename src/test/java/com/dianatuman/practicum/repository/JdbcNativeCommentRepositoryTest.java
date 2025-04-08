package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.configuration.TestDataSourceConfiguration;
import com.dianatuman.practicum.model.Comment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestDataSourceConfiguration.class)
public class JdbcNativeCommentRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("insert into posts(id, title, post_text) values (100, 'FIRST POST', 'FIRST TEXT')");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text) VALUES (100, 100, 'FIRST COMMENT')");
    }

    @Test
    void add_shouldSaveCommentInDatabase() {
        long id = commentRepository.addComment(100, "TESTCOMMENT_ADD");
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
        commentRepository.editComment(100, "EDITED_TEXT");

        var comments = getAllComments();
        Comment editedComment = comments.stream()
                .filter(c -> c.getId() == 100)
                .findFirst()
                .orElse(null);

        assertNotNull(editedComment);
        assertEquals("EDITED_TEXT", editedComment.getText());
    }

    @Test
    void delete_shouldDeleteCommentInDatabase() {
        commentRepository.deleteComment(100);

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

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
    }

}
