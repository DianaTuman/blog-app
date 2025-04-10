package com.dianatuman.practicum.service;

import com.dianatuman.practicum.BlogAppApplication;
import com.dianatuman.practicum.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BlogAppApplication.class)
public class PostServiceTest extends ServiceTest {

    @Test
    public void getPosts_shouldGetAllPosts() throws Exception {
        Object modelPosts = Objects.requireNonNull(mockMvc.perform(get("/posts"))
                .andReturn().getModelAndView()).getModel().get("posts");
        if (modelPosts instanceof List) {
            List<Post> posts = (List<Post>) modelPosts;
            assertThat(posts).isNotEmpty();
            assertThat(posts).hasSize(1);
            assertThat(posts)
                    .contains(new Post(1, "FIRST POST", "FIRST TEXT", 0, "TAG1 TAG2", 1));
        } else {
            fail();
        }
    }

    @Test
    public void postPosts_shouldAddPost() throws Exception {
        MockHttpServletRequestBuilder request = multipart("/posts")
                .file(new MockMultipartFile("image", "", "image/png", new byte[0]))
                .part(new MockPart("title", "NEW_TITLE".getBytes()))
                .part(new MockPart("text", "NEW_TEXT".getBytes()))
                .part(new MockPart("tags", "tag".getBytes()));

        var response = Objects.requireNonNull(mockMvc.perform(request).andReturn().getResponse().getRedirectedUrl())
                .replace("/posts/", "");
        assertThat(postService.getPosts())
                .contains(new Post(Long.parseLong(response), "NEW_TITLE", "NEW_TEXT", 0, "tag", 0));
    }

    @Test
    public void getPostId_shouldGetPost() throws Exception {
        Post post = (Post) Objects.requireNonNull(mockMvc.perform(get("/posts/1"))
                .andReturn().getModelAndView()).getModel().get("post");
        assertThat(post).isEqualTo(
                new Post(1, "FIRST POST", "FIRST TEXT", 0, "TAG1 TAG2", 1));
    }

    @Test
    public void postPostIdLike_shouldUpdateLikeCount() throws Exception {
        assertThat(postService.getPost(1).getLikesCount()).isZero();
        mockMvc.perform(post("/posts/1/like")
                .param("like", "true"));
        assertThat(postService.getPost(1).getLikesCount()).isEqualTo(1);
        mockMvc.perform(post("/posts/1/like")
                .param("like", "false"));
        assertThat(postService.getPost(1).getLikesCount()).isZero();
    }

    @Test
    public void postPostIdDelete_shouldDeletePost() throws Exception {
        mockMvc.perform(post("/posts/1/delete"));

        assertThat(postService.getPosts()).isEmpty();
    }

    @Test
    public void getPostIdEdit_shouldGetPost() throws Exception {
        mockMvc.perform(get("/posts/1/edit"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//button").string("Редактировать"));

        Post post = (Post) Objects.requireNonNull(mockMvc.perform(get("/posts/1"))
                .andReturn().getModelAndView()).getModel().get("post");
        assertThat(post).isEqualTo(
                new Post(1, "FIRST POST", "FIRST TEXT", 0, "TAG1 TAG2", 1));
    }

    @Test
    public void postPostId_shouldEditPost() throws Exception {
        MockHttpServletRequestBuilder request = multipart("/posts/1")
                .file(new MockMultipartFile("image", "", "image/png", new byte[0]))
                .part(new MockPart("title", "NEW_TITLE".getBytes()))
                .part(new MockPart("text", "NEW_TEXT".getBytes()))
                .part(new MockPart("tags", "new_tag".getBytes()));
        mockMvc.perform(request);

        assertThat(postService.getPosts())
                .doesNotContain(new Post(1, "FIRST POST", "FIRST TEXT", 0, "TAG1 TAG2'", 1));
        assertThat(postService.getPosts()).contains(new Post(1, "NEW_TITLE", "NEW_TEXT", 0, "new_tag", 1));
    }
}
