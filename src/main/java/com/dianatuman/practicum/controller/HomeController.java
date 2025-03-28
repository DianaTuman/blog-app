package com.dianatuman.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    @ResponseBody
    public String homePage() {
        // GET "/" - редирект на "/posts"
        return "redirect:/posts";
    }

    @GetMapping("/images/{id}")
    @ResponseBody
    public void getImage() {
        //GET "/images/{id}" -эндпоин, возвращающий набор байт картинки поста
        //       		Параметры:
        //       			"id" - идентификатор поста
    }
}
