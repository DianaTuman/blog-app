package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.configuration.DataSourceConfiguration;
import com.dianatuman.practicum.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({DataSourceConfiguration.class, JdbcNativePostRepository.class})
@TestPropertySource("classpath:application.properties")
public class JdbcNativePostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM comments");
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("insert into posts(id, title, post_text) values ('1', 'FIRST POST', 'FIRST TEXT')");
        jdbcTemplate.execute("INSERT INTO comments (id, post_id, text) VALUES (1, 1, 'FIRST COMMENT')");
    }

    @Test
    void findAll_shouldReturnAllPosts() {
        var post = postRepository.getPost(addNewPost());
        List<Post> posts = postRepository.findAll();

        assertNotNull(posts);
        assertFalse(posts.isEmpty());
        assertTrue(posts.stream().anyMatch(p -> p.equals(post)));
    }

    @Test
    void add_shouldSavePostInDatabase() {
        long id = postRepository.addPost(new Post("TEST_POST", "TEXT", new byte[0], "test test"));
        assertNotEquals(0, id);

        Post addedPost = postRepository.getPost(id);
        assertNotNull(addedPost);
        assertEquals(0, addedPost.getLikesCount());
        assertEquals("TEST_POST", addedPost.getTitle());
        assertEquals("TEXT", addedPost.getText());
        assertEquals("test test", addedPost.getTagsAsText());
    }

    @Test
    void edit_shouldUpdatePostInDatabase() {
        long id = addNewPost();
        postRepository.editPost(id, new Post("EDITED_POST", "EDITED_TEXT", new byte[0], "edited tags"));

        Post editedPost = postRepository.getPost(id);
        assertNotNull(editedPost);
        assertEquals("EDITED_POST", editedPost.getTitle());
        assertEquals("EDITED_TEXT", editedPost.getText());
        assertEquals("edited tags", editedPost.getTagsAsText());
    }

    @Test
    void delete_shouldDeletePostInDatabase() {
        long id = addNewPost();
        postRepository.deletePost(id);

        Post deletedPost = postRepository.getPost(id);
        assertNull(deletedPost);
    }

    @Test
    void get_shouldGetPostInDatabase() {
        long id = addNewPost();
        Post post = postRepository.getPost(id);

        var posts = postRepository.findAll();
        Post expectedPost = posts.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        assertEquals(expectedPost, post);
    }

    @Test
    void updateLike_shouldUpdatePostLikesCountInDatabase() {
        long id = addNewPost();
        assertEquals(0, postRepository.getPost(id).getLikesCount());

        postRepository.updateLikes(id, 1);
        assertEquals(1, postRepository.getPost(id).getLikesCount());

        postRepository.updateLikes(id, -2);
        assertEquals(-1, postRepository.getPost(id).getLikesCount());

        postRepository.updateLikes(id, 100);
        assertEquals(99, postRepository.getPost(id).getLikesCount());
    }

    private long addNewPost() {
        return postRepository.addPost(new Post("TEST_POST", "TEXT", new byte[0], "test test"));
    }
}
