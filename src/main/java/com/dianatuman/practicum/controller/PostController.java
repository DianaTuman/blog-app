package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.model.Post;
import com.dianatuman.practicum.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/posts/{id}")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * GET "/posts/{id}" - страница с постом
     *
     * @param id    - идентификатор поста
     * @param model - "post" - модель поста (id, title, text, imagePath, likesCount, comments)
     * @return - шаблон "post.html"
     */
    @GetMapping
    public String postPage(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "post";
    }

    /**
     * POST "/posts/{id}/like" - эндпоинт увеличение/уменьшение числа лайков поста
     *
     * @param id   - идентификатор поста
     * @param like - если true, то +1 лайк, если "false", то -1 лайк
     * @return редирект на "/posts/{id}"
     */
    @PostMapping("/like")
    public String like(@PathVariable(name = "id") long id, @ModelAttribute("like") Boolean like) {
        postService.likePost(id, like);
        return String.format("redirect:/posts/%s", id);
    }

    /**
     * GET "/posts/{id}/edit" - страница редактирования поста
     *
     * @param id    - идентификатор поста
     * @param model - "post" - модель поста (id, title, text, imagePath, likesCount, comments)
     * @return редирект на форму редактирования поста "add-post.html"
     */
    @GetMapping("/edit")
    public String editPage(@PathVariable(name = "id") long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "add-post";
    }


    /**
     * POST "/posts/{id}" - эндпоинт редактирование поста
     * Принимает:"multipart/form-data"
     *
     * @param id    - идентификатор поста
     * @param title - название поста
     * @param text  - текст поста
     * @param image - файл картинки поста (класс MultipartFile, может быть null - значит, остается прежним)
     * @param tags  - список тегов поста (по умолчанию, пустая строка)
     * @return редирект на отредактированный "/posts/{id}"
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String edit(@PathVariable(name = "id") long id, @RequestPart("title") String title, @RequestPart("text") String text,
                       @RequestPart("image") MultipartFile image, @Nullable @RequestPart("tags") String tags) throws IOException {
        Post updatedPost = new Post(title, text, image.getBytes(), tags);
        postService.editPost(id, updatedPost);
        return String.format("redirect:/posts/%s", id);
    }

    /**
     * POST "/posts/{id}/delete" - эндпоинт удаления поста
     *
     * @param id - идентификатор поста
     * @return редирект на "/posts"
     */
    @PostMapping("/delete")
    public String delete(@PathVariable(name = "id") long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }
}