package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HomeController {

    private final PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    /**
     * @return редирект на "/posts"
     */
    @GetMapping
    public String homePage() {
        return "redirect:./posts";
    }


    /**
     * @param id - идентификатор поста
     * @return набор байт картинки поста
     */
    @GetMapping("/images/{id}")
    @ResponseBody
    public byte[] getImage(@PathVariable(name = "id") long id) {
        return postService.getImage(id);
    }
}
