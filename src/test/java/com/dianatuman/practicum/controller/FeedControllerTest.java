package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.configuration.DataSourceConfiguration;
import com.dianatuman.practicum.configuration.WebConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        jdbcTemplate.execute("insert into posts(id, title, post_text) values ('1', 'FIRST POST', 'FIRST TEXT')");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text) VALUES (1, 1, 'FIRST COMMENT')");
    }

    @Test
    public void getPosts_shouldReturnFeedPage() throws Exception {
        mockMvc.perform(get("/posts")
                .param("search", ""))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(xpath("//h2").string("FIRST POST"));
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
//        mockMvc.perform(post("/posts")
//                        .requestAttr("title", "NEW_TITLE")
//                        .requestAttr("text", "NEW_TEXT")
//                        .requestAttr("tags", "tag")
//                        .requestAttr("image", ""))
//                .andExpect(status().is3xxRedirection());
    }
}
