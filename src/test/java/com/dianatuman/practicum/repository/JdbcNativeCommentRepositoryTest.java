package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.model.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcNativeCommentRepositoryTest extends JdbcNativeRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void add_shouldSaveCommentInDatabase() {
        long id = commentRepository.addComment(postId, "TESTCOMMENT_ADD");
        assertThat(id).isNotZero();

        var comments = getAllComments();
        Comment addedComment = comments.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);

        assertThat(addedComment).isNotNull();
        assertThat(addedComment.getText()).isEqualTo("TESTCOMMENT_ADD");
    }

    @Test
    void edit_shouldUpdateCommentInDatabase() {
        commentRepository.editComment(commentId, "EDITED_TEXT");

        var comments = getAllComments();
        Comment editedComment = comments.stream()
                .filter(c -> c.getId() == commentId)
                .findFirst()
                .orElse(null);

        assertThat(editedComment).isNotNull();
        assertThat(editedComment.getText()).isEqualTo("EDITED_TEXT");
    }

    @Test
    void delete_shouldDeleteCommentInDatabase() {
        commentRepository.deleteComment(commentId);

        var comments = getAllComments();
        Comment deletedComment = comments.stream()
                .filter(c -> c.getId() == commentId)
                .findFirst()
                .orElse(null);

        assertThat(deletedComment).isNull();
    }

}
