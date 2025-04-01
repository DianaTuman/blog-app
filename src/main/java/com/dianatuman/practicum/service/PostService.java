package com.dianatuman.practicum.service;

import com.dianatuman.practicum.model.Post;
import com.dianatuman.practicum.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public Post getPost(long id) {
        return postRepository.getPost(id);
    }

    public long addPost(Post newPost) {
        return postRepository.addPost(newPost);
    }

    public void editPost(long id, Post updatedPost) {
        postRepository.editPost(id, updatedPost);
    }

    public void deletePost(long id) {
        postRepository.deletePost(id);
    }

    public void likePost(long id, boolean like) {
        postRepository.updateLikes(id, like ? 1 : -1);
    }

    public byte[] getImage(long id) {
        return postRepository.getImage(id);
    }

    public List<Post> getPostsByTag(String search) {
        return getPosts().stream().filter(p -> p.getTagsAsText().toLowerCase()
                .contains(search.toLowerCase())).toList();
//        TODO
//        it's most likely faster to do filtering in DB on large data
//        return postRepository.findPostsByTag(search);
    }
}
