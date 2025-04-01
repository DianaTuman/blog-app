package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.configuration.DataSourceConfiguration;
import com.dianatuman.practicum.configuration.WebConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig({DataSourceConfiguration.class, WebConfiguration.class})
@TestPropertySource("classpath:application.properties")
public class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
                .andExpect(xpath("//span[@id='comment1']").string("FIRST COMMENT"))
        ;
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
    }

    @Test
    public void postPostIdDelete_shouldDeletePostAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    public void getPostIdEdit_shouldReturnEditPostPage() throws Exception {
        mockMvc.perform(get("/posts/1/edit"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//button").string("Редактировать"));
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
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
    }

}
