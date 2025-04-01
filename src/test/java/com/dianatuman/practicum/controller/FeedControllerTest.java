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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig({DataSourceConfiguration.class, WebConfiguration.class})
@TestPropertySource("classpath:application.properties")
public class FeedControllerTest {

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
        MockHttpServletRequestBuilder request = multipart("/posts")
                .file(new MockMultipartFile("image", "", "image/png", new byte[0]))
                .part(new MockPart("title", "NEW_TITLE".getBytes()))
                .part(new MockPart("text", "NEW_TEXT".getBytes()))
                .part(new MockPart("tags", "tag".getBytes()));

        mockMvc.perform(request).andExpect(status().is3xxRedirection());
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
    }
}
