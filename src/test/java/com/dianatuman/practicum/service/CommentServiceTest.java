package com.dianatuman.practicum.service;

import com.dianatuman.practicum.BlogAppApplication;
import com.dianatuman.practicum.model.Comment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BlogAppApplication.class)
public class CommentServiceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostService postService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("insert into posts(id, title, post_text) values ('1', 'FIRST POST', 'FIRST TEXT')");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text) VALUES (1, 1, 'FIRST COMMENT')");
    }

    @Test
    public void postComments_shouldAddCommentAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/comments")
                        .param("text", "TESTCOMMENT_ADD"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        List<Comment> comments = postService.getPost(1).getComments();
        assertThat(comments).hasSize(2);
        assertThat(comments).anyMatch(comment -> comment.getText().equals("TESTCOMMENT_ADD"));
    }

    @Test
    public void postCommentId_shouldEditCommentAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/comments/1")
                        .param("text", "EDITED_TESTCOMMENT1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        List<Comment> comments = postService.getPost(1).getComments();
        assertThat(comments).hasSize(1);
        assertThat(comments.getFirst().getText()).isEqualTo("EDITED_TESTCOMMENT1");
    }

    @Test
    public void postCommentIdDelete_shouldDeleteCommentAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/comments/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        List<Comment> comments = postService.getPost(1).getComments();
        assertThat(comments).isEmpty();
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
    }
}
