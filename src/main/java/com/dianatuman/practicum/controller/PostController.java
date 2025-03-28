package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts/{id}")
public class PostController {

//    private final PostService postService;
//
//    public PostController(PostService postService) {
//        this.postService = postService;
//    }

    @GetMapping
    public String postPage(@PathVariable Long id, Model model) {
        //GET "/posts/{id}" - страница с постом
        //        	Возвращает:
        //       			шаблон "post.html"
        //       			используется модель для заполнения шаблона:
        //       				"post" - модель поста (id, title, text, imagePath, likesCount, comments)
//        model.addAttribute("post", postService.getPost(id));
        return "post";
    }

    @PostMapping("/like")
    public String like(@PathVariable Long id) {
        // POST "/posts/{id}/like" - увеличение/уменьшение числа лайков поста
        //       		Параметры:
        //       			"id" - идентификатор поста
        //       			"like" - если true, то +1 лайк, если "false", то -1 лайк
        //       		Возвращает:
        //       			редирект на "/posts/{id}"
//        postService.likePost(id, );
        return String.format("redirect:/posts/%s", id);
    }

    @PostMapping("/edit")
    public String editPage() {
        //POST "/posts/{id}/edit" - страница редактирования поста
        //       		Параметры:
        //       			"id" - идентификатор поста
        //       		Возвращает:
        //       			редирект на форму редактирования поста "add-post.html"
        //       			используется модель для заполнения шаблона:
        //       				"post" - модель поста (id, title, text, imagePath, likesCount, comments)
        return "add-post";
    }

    @PostMapping
    public String edit(@PathVariable Long id) {
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
//        postService.editPost(id);
        return String.format("redirect:/posts/%s", id);
    }

    @PostMapping("/delete")
    public String delete(@PathVariable Long id) {
        //POST "/posts/{id}/delete" - эндпоинт удаления поста
        //       		Параметры:
        //       			"id" - идентификатор поста
        //       		Возвращает:
        //       			редирект на "/posts"
//        postService.deletePost(id);
        return "redirect:/posts";
    }
}