package com.dianatuman.practicum.service;

import com.dianatuman.practicum.BlogAppApplication;
import com.dianatuman.practicum.model.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = BlogAppApplication.class)
public class CommentServiceTest extends ServiceTest {

    @Test
    public void postComments_shouldSaveComment() throws Exception {
        mockMvc.perform(post("/posts/1/comments")
                .param("text", "TESTCOMMENT_ADD"));

        List<Comment> comments = postService.getPost(1).getComments();
        assertThat(comments).hasSize(2);
        assertThat(comments).anyMatch(comment -> comment.getText().equals("TESTCOMMENT_ADD"));
    }

    @Test
    public void postCommentId_shouldEditComment() throws Exception {
        mockMvc.perform(post("/posts/1/comments/1")
                .param("text", "EDITED_TESTCOMMENT1"));

        List<Comment> comments = postService.getPost(1).getComments();
        assertThat(comments).hasSize(1);
        assertThat(comments.getFirst().getText()).isEqualTo("EDITED_TESTCOMMENT1");
    }

    @Test
    public void postCommentIdDelete_shouldDeleteComment() throws Exception {
        mockMvc.perform(post("/posts/1/comments/1/delete"));

        List<Comment> comments = postService.getPost(1).getComments();
        assertThat(comments).isEmpty();
    }
}
