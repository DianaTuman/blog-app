package com.dianatuman.practicum.repository;

import com.dianatuman.practicum.model.Post;

import java.util.List;

public interface PostRepository {

    List<Post> findAll();

    long addPost(Post newPost);

    Post getPost(long id);

    void editPost(long id, Post updatedPost);

    void deletePost(long id);

    void updateLikes(long id, int likes);

    byte[] getImage(long id);

    List<Post> findPostsByTag(String search);
}
