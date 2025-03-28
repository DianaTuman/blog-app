package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts/{id}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public String postComment(@PathVariable Long id) {
        // POST "/posts/{id}/comments" - эндпоинт добавления комментария к посту
        //       		Параметры:
        //       			"id" - идентификатор поста
        //       			"text" - текст комментария
        //       		Возвращает:
        //       			редирект на "/posts/{id}"
        commentService.addComment(id, "text");
        return String.format("redirect:/posts/%s", id);
    }

    @PostMapping("/{commentId}")
    public String editComment(@PathVariable Long id, @PathVariable Long commentId) {
        //  POST "/posts/{id}/comments/{commentId}" - эндпоинт редактирования комментария
        //       		Параметры:
        //       			"id" - идентификатор поста
        //       			"commentId" - идентификатор комментария
        //       			"text" - текст комментария
        //       		Возвращает:
        //       			редирект на "/posts/{id}"
        commentService.editComment(id, commentId, "text");
        return String.format("redirect:/posts/%s", id);
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long id, @PathVariable Long commentId) {
        // POST "/posts/{id}/comments/{commentId}/delete" - эндпоинт удаления комментария
        //       		Параметры:
        //       			"id" - идентификатор поста
        //       			"commentId" - идентификатор комментария
        //       		Возвращает:
        //       			редирект на "/posts/{id}"
        commentService.deleteComment(id, commentId);
        return String.format("redirect:/posts/%s", id);
    }

}
