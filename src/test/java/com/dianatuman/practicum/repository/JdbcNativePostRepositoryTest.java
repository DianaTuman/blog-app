package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcNativePostRepositoryTest extends JdbcNativeRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void findAll_shouldReturnAllPosts() {
        var post = postRepository.getPost(postId);
        List<Post> posts = postRepository.findAll();

        assertThat(posts).isNotNull();
        assertThat(posts).isNotEmpty();
        assertThat(posts).contains(post);
    }

    @Test
    void add_shouldSavePostInDatabase() {
        long id = postRepository.addPost(new Post("TEST_POST", "TEXT", new byte[0], "test test"));
        assertThat(id).isNotZero();

        Post addedPost = postRepository.getPost(id);
        assertThat(addedPost).isNotNull();
        assertThat(addedPost.getLikesCount()).isZero();
        assertThat(addedPost.getTitle()).isEqualTo("TEST_POST");
        assertThat(addedPost.getText()).isEqualTo("TEXT");
        assertThat(addedPost.getTagsAsText()).isEqualTo("test test");
    }

    @Test
    void edit_shouldUpdatePostInDatabase() {
        postRepository.editPost(postId, new Post("EDITED_POST", "EDITED_TEXT", new byte[0], "edited tags"));

        Post editedPost = postRepository.getPost(postId);
        assertThat(editedPost).isNotNull();
        assertThat(editedPost.getLikesCount()).isZero();
        assertThat(editedPost.getTitle()).isEqualTo("EDITED_POST");
        assertThat(editedPost.getText()).isEqualTo("EDITED_TEXT");
        assertThat(editedPost.getTagsAsText()).isEqualTo("edited tags");
    }

    @Test
    void delete_shouldDeletePostInDatabase() {
        postRepository.deletePost(postId);

        Post deletedPost = postRepository.getPost(postId);
        assertThat(deletedPost).isNull();
    }

    @Test
    void get_shouldGetPostInDatabase() {
        Post post = postRepository.getPost(postId);

        var posts = postRepository.findAll();
        Post expectedPost = posts.stream()
                .filter(p -> p.getId() == postId)
                .findFirst()
                .orElse(null);

        assertThat(post).isEqualTo(expectedPost);
    }

    @Test
    void updateLike_shouldUpdatePostLikesCountInDatabase() {
        assertThat(postRepository.getPost(postId).getLikesCount()).isEqualTo(0);

        postRepository.updateLikes(postId, 1);
        assertThat(postRepository.getPost(postId).getLikesCount()).isEqualTo(1);

        postRepository.updateLikes(postId, -2);
        assertThat(postRepository.getPost(postId).getLikesCount()).isEqualTo(-1);

        postRepository.updateLikes(postId, 100);
        assertThat(postRepository.getPost(postId).getLikesCount()).isEqualTo(99);
    }
}
