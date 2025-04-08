package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.model.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcNativeCommentRepositoryTest extends JdbcNativeRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

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

}
