package com.dianatuman.practicum.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FeedController.class)
public class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Test
    public void getPosts_shouldReturnFeedPage() throws Exception {
        when(postService.getPosts()).thenReturn(
                List.of(new Post(1, "FIRST POST", "FIRST TEXT", 0, "TAG1 TAG2", 1)));
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(xpath("//h2").string("FIRST POST"))
                .andExpect(xpath("//a[@href='./posts/1']").exists())
                .andExpect(xpath("//tr[2]/td[1]/p[2]").string("FIRST TEXT"))
                .andExpect(xpath("//tr/td/p[4]/span").string("#TAG1 "))
                .andExpect(xpath("//tr/td/p[4]/span[2]").string("#TAG2 "));

        verify(postService, times(1)).getPosts();
    }

    @Test
    public void getPostsAdd_shouldReturnAddPostPage() throws Exception {
        mockMvc.perform(get("/posts/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-post"))
                .andExpect(model().attributeDoesNotExist("post"))
                .andExpect(xpath("//button").string("Сохранить"));
    }

    @Test
    public void postPosts_shouldAddPostAndRedirect() throws Exception {
        Post newPost = new Post("NEW_TITLE", "NEW_TEXT", new byte[0], "tag");
        when(postService.addPost(newPost)).thenReturn(1L);
        MockHttpServletRequestBuilder request = multipart("/posts")
                .file(new MockMultipartFile("image", "", "image/png", new byte[0]))
                .part(new MockPart("title", "NEW_TITLE".getBytes()))
                .part(new MockPart("text", "NEW_TEXT".getBytes()))
                .part(new MockPart("tags", "tag".getBytes()));

        mockMvc.perform(request).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
        verify(postService, times(1)).addPost(newPost);
    }
}
