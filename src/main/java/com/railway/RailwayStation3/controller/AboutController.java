package com.railway.RailwayStation3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для отображения страницы "Об авторе".
 */
@Controller
public class AboutController {
    @GetMapping("/about")
    public String showAboutAuthor() {
        return "main/about";
    }
}