package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.model.Post;
import com.dianatuman.practicum.service.PostService;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class FeedController {

    private final PostService postService;

    public FeedController(PostService postService) {
        this.postService = postService;
    }

    /**
     * GET "posts" - список постов на странице ленты постов
     *
     * @param model      search - строка с поиском по тегу поста (по умолчанию, пустая строка - все посты)
     * @param pageSize   - максимальное число постов на странице (по умолчанию, 10)
     * @param pageNumber - номер текущей страницы (по умолчанию, 1)
     * @return шаблон "posts.html" используется модель для заполнения шаблона:
     */
    @GetMapping
    public String feedPage(Model model, @ModelAttribute("search") String search,
                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                           @RequestParam(name = "pageNumber", defaultValue = "1") Integer pageNumber) {
        List<Post> posts;
        if (search.isBlank()) {
            posts = postService.getPosts();
        } else {
            posts = postService.getPostsByTag(search);
        }

        //Maybe do pagination within DB
        PagedListHolder<Post> pagedListHolder = new PagedListHolder<>(posts);
        pagedListHolder.setPageSize(pageSize);
        pagedListHolder.setPage(pageNumber);

        model.addAttribute("posts", pagedListHolder.getPageList());
        model.addAttribute("paging", pagedListHolder);
        return "posts";
    }

    /**
     * GET "/posts/add" - страница добавления поста
     *
     * @return шаблон "add-post.html"
     */
    @GetMapping("/add")
    public String addPostPage() {
        return "add-post";
    }

    /**
     * POST "/posts" - добавление поста
     * Принимает: "multipart/form-data"
     *
     * @param title - название поста
     * @param text  - текст поста
     * @param image - файл картинки поста (класс MultipartFile)
     * @param tags  - список тегов поста (по умолчанию, пустая строка)
     * @return редирект на созданный "/posts/{id}"
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String addPost(@RequestPart("title") String title, @RequestPart("text") String text,
                          @RequestPart("image") MultipartFile image, @Nullable @RequestPart("tags") String tags) throws IOException {
        Post newPost = new Post(title, text, image.getBytes(), tags);
        long id = postService.addPost(newPost);
        return String.format("redirect:/posts/%s", id);
    }

}
