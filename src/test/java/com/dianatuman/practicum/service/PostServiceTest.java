package com.dianatuman.practicum.service;

import com.dianatuman.practicum.BlogAppApplication;
import com.dianatuman.practicum.model.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BlogAppApplication.class)
public class PostServiceTest {

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
        jdbcTemplate.execute("insert into posts(id, title, post_text, tags) values ('1', 'FIRST POST', 'FIRST TEXT', 'TAG1 TAG2')");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text) VALUES (1, 1, 'FIRST COMMENT')");
    }

    @Test
    public void getPosts_shouldReturnFeedPage() throws Exception {
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
    }

    @Test
    public void postPosts_shouldAddPostAndRedirect() throws Exception {
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
    public void getPostId_shouldReturnPostPage() throws Exception {
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
    }

    @Test
    public void postPostIdLike_shouldUpdateLikeCountAndRedirect() throws Exception {
        assertThat(postService.getPost(1).getLikesCount()).isZero();
        mockMvc.perform(post("/posts/1/like")
                        .param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
        assertThat(postService.getPost(1).getLikesCount()).isEqualTo(1);
        mockMvc.perform(post("/posts/1/like")
                        .param("like", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
        assertThat(postService.getPost(1).getLikesCount()).isZero();
    }

    @Test
    public void postPostIdDelete_shouldDeletePostAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        assertThat(postService.getPosts()).isEmpty();
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
        assertThat(postService.getPosts())
                .doesNotContain(new Post(1, "FIRST POST", "FIRST TEXT", 0, "TAG1 TAG2'", 1));
        assertThat(postService.getPosts()).contains(new Post(1, "NEW_TITLE", "NEW_TEXT", 0, "new_tag", 1));
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
    }
}
