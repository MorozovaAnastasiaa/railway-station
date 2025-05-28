package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.repository.User;
import com.railway.RailwayStation3.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Контроллер, отвечающий за аутентификацию(вход/регистрация).
 */
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отображает форму входа.
     * Используется Spring Security по умолчанию.
     *
     * @return имя шаблона "login"
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * Отображает форму регистрации нового пользователя.
     * Добавляет пустой объект User в модель для привязки формы.
     *
     * @param model модель данных
     * @return имя шаблона "register"
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Обрабатывает регистрацию нового пользователя.
     * При ошибке валидации возвращает форму с сообщением об ошибке.
     *
     * @param user данные пользователя из формы
     * @param model модель данных
     * @return редирект на /login при успехе или повторное отображение формы при ошибке
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerUser(user);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}