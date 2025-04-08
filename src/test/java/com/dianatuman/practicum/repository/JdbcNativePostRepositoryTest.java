package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcNativePostRepositoryTest extends JdbcNativeRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    private final long id = 100;

    @Test
    void findAll_shouldReturnAllPosts() {
        var post = postRepository.getPost(id);
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
        postRepository.editPost(id, new Post("EDITED_POST", "EDITED_TEXT", new byte[0], "edited tags"));

        Post editedPost = postRepository.getPost(id);
        assertNotNull(editedPost);
        assertEquals("EDITED_POST", editedPost.getTitle());
        assertEquals("EDITED_TEXT", editedPost.getText());
        assertEquals("edited tags", editedPost.getTagsAsText());
    }

    @Test
    void delete_shouldDeletePostInDatabase() {
        postRepository.deletePost(id);

        Post deletedPost = postRepository.getPost(id);
        assertNull(deletedPost);
    }

    @Test
    void get_shouldGetPostInDatabase() {
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
        assertEquals(0, postRepository.getPost(id).getLikesCount());

        postRepository.updateLikes(id, 1);
        assertEquals(1, postRepository.getPost(id).getLikesCount());

        postRepository.updateLikes(id, -2);
        assertEquals(-1, postRepository.getPost(id).getLikesCount());

        postRepository.updateLikes(id, 100);
        assertEquals(99, postRepository.getPost(id).getLikesCount());
    }
}
