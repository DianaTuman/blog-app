package com.dianatuman.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class FeedController {

    @GetMapping
    public String feedPage() {
        return "posts";
    }

    @GetMapping("/{postId}")
    public String postPage() {
        return "posts";
    }

    @GetMapping("/add")
    public String addPostPage() {
        return "add-post";
    }
}
