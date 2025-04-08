package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.model.Comment;
import com.dianatuman.practicum.model.Post;
import com.dianatuman.practicum.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    public void getPostId_shouldReturnPostPage() throws Exception {
        Post testPost = new Post(1, "FIRST POST", "FIRST TEXT", 0, "TAG1 TAG2", 1);
        testPost.setComments(List.of(new Comment(1, "FIRST COMMENT")));
        when(postService.getPost(1)).thenReturn(testPost);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//h2").string("FIRST POST"))
                .andExpect(xpath("//tr[3]/td").string("FIRST TEXT"))
                .andExpect(xpath("//tr[2]/td/p/span").string("#TAG1 "))
                .andExpect(xpath("//tr[2]/td/p/span[2]").string("#TAG2 "))
                .andExpect(xpath("//form[@method='GET' and @action='./1/edit']").exists())
                .andExpect(xpath("//form[@method='POST' and @action='./1/delete']").exists())
                .andExpect(xpath("//form[@method='POST' and @action='./1/like']").exists())
                .andExpect(xpath("//form[@method='POST' and @action='./1/comments']").exists())
                .andExpect(xpath("//form[@method='POST' and @action='./1/comments/1']").exists())
                .andExpect(xpath("//form[@method='POST' and @action='./1/comments/1/delete']").exists())
                .andExpect(xpath("//span[@id='comment1']").string("FIRST COMMENT"));

        verify(postService, times(1)).getPost(1);
    }


    @Test
    public void postPostIdLike_shouldUpdateLikeCountAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/like")
                        .param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
        mockMvc.perform(post("/posts/1/like")
                        .param("like", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postService, times(1)).likePost(1, true);
        verify(postService, times(1)).likePost(1, false);
    }

    @Test
    public void postPostIdDelete_shouldDeletePostAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(postService, times(1)).deletePost(1);
    }

    @Test
    public void getPostIdEdit_shouldReturnEditPostPage() throws Exception {
        Post testPost = new Post(1, "FIRST POST", "FIRST TEXT", 0, "TAG1 TAG2", 1);
        when(postService.getPost(1)).thenReturn(testPost);

        mockMvc.perform(get("/posts/1/edit"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//button").string("Редактировать"));

        verify(postService, times(1)).getPost(1);
    }

    @Test
    public void postPostId_shouldEditPostAndRedirect() throws Exception {
        MockHttpServletRequestBuilder request = multipart("/posts/1")
                .file(new MockMultipartFile("image", "", "image/png", new byte[0]))
                .part(new MockPart("title", "NEW_TITLE".getBytes()))
                .part(new MockPart("text", "NEW_TEXT".getBytes()))
                .part(new MockPart("tags", "new_tag".getBytes()));

        mockMvc.perform(request).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postService, times(1)).editPost(1,
                new Post("NEW_TITLE", "NEW_TEXT", new byte[0], "new_tag"));
    }
}
