package com.dianatuman.practicum.controller;

import com.dianatuman.practicum.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class FeedController {

    private final PostService postService;

    public FeedController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String feedPage(Model model) {
        // GET "posts" - список постов на странице ленты постов
        //		Параметры:
        //			search - строка с поиском по тегу поста (по умолчанию, пустая строка - все посты)
        //            		pageSize - максимальное число постов на странице (по умолчанию, 10)
        //            		pageNumber - номер текущей страницы (по умолчанию, 1)
        //            	Возвращает:
        //            		шаблон "posts.html"
        //            		используется модель для заполнения шаблона:
        //            			"posts" - List<Post> - список постов (id, title, text, imagePath, likesCount, comments)
        //            			"search" - строка поиска (по умолчанию, пустая строка - все посты)
        //            			"paging":
        //            				"pageNumber" - номер текущей страницы (по умолчанию, 1)
        //            				"pageSize" - максимальное число постов на странице (по умолчанию, 10)
        //            				"hasNext" - можно ли пролистнуть вперед
        //            				"hasPrevious" - можно ли пролистнуть назад
//        model.getAttribute("search");
//        model.getAttribute("paging");
        model.addAttribute("posts", postService.getPosts());
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

    @PostMapping
    public String addPost() {
        //POST "/posts" - добавление поста
        //       		Принимает:
        //       			"multipart/form-data"
        //       		Параметры:
        //       			"title" - название поста
        //       			"text" - текст поста
        //       			"image" - файл картинки поста (класс MultipartFile)
        //       			"tags" - список тегов поста (по умолчанию, пустая строка)
        //       		Возвращает:
        //       			редирект на созданный "/posts/{id}"
        //        Post newPost = new Post();
//        postService.addPost(newPost);
        return "redirect:/posts/{id}";
    }

}
