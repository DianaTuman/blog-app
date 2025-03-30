package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    /**
     * POST "/posts/{id}/comments" - эндпоинт добавления комментария к посту
     *
     * @param id   - идентификатор поста
     * @param text - текст комментария
     * @return редирект на "/posts/{id}"
     */
    @PostMapping
    public String postComment(@PathVariable(name = "id") long id, @ModelAttribute("text") String text) {
        commentService.addComment(id, text);
        return String.format("redirect:/posts/%s", id);
    }

    /**
     * POST "/posts/{id}/comments/{commentId}" - эндпоинт редактирования комментария
     *
     * @param id        - идентификатор поста
     * @param commentId - идентификатор комментария
     * @param text      - текст комментария
     * @return редирект на "/posts/{id}"
     */
    @PostMapping("/{commentId}")
    public String editComment(@PathVariable(name = "id") long id, @PathVariable(name = "commentId") long commentId,
                              @ModelAttribute("text") String text) {
        commentService.editComment(commentId, text);
        return String.format("redirect:/posts/%s", id);
    }

    /**
     * POST "/posts/{id}/comments/{commentId}/delete" - эндпоинт удаления комментария
     *
     * @param id        - идентификатор поста
     * @param commentId - идентификатор комментария
     * @return редирект на "/posts/{id}"
     */
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable(name = "id") long id, @PathVariable(name = "commentId") long commentId) {
        commentService.deleteComment(commentId);
        return String.format("redirect:/posts/%s", id);
    }

}
