package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.model.Post;
import com.dianatuman.practicum.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     * POST "/posts/{id}/like" - увеличение/уменьшение числа лайков поста
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


    @PostMapping
    public String edit(@PathVariable(name = "id") long id, @ModelAttribute("post") Post post) {
        // POST "/posts/{id}" - редактирование поста
        //       		Принимает:
        //       			"multipart/form-data"
        //       		Параметры:
        //       			"id" - идентификатор поста
        //       			"title" - название поста
        //       			"text" - текст поста
        //       			"image" - файл картинки поста (класс MultipartFile, может быть null - значит, остается прежним)
        //       			"tags" - список тегов поста (по умолчанию, пустая строка)
        //       		Возвращает:
        //       			редирект на отредактированный "/posts/{id}"
        postService.editPost(id, post);
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