package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Test
    public void postComments_shouldAddCommentAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/comments")
                        .param("text", "TESTCOMMENT_ADD"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(commentService, times(1)).addComment(1, "TESTCOMMENT_ADD");
    }

    @Test
    public void postCommentId_shouldEditCommentAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/comments/1")
                        .param("text", "EDITED_TESTCOMMENT1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(commentService, times(1)).editComment(1, "EDITED_TESTCOMMENT1");
    }

    @Test
    public void postCommentIdDelete_shouldDeleteCommentAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/comments/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(commentService, times(1)).deleteComment(1);

    }
}
