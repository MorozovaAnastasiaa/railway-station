package com.railway.RailwayStation3.controller;

import com.railway.RailwayStation3.repository.Train;
import com.railway.RailwayStation3.repository.User;
import com.railway.RailwayStation3.service.TrainService;
import com.railway.RailwayStation3.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер, предоставляющий функционал администрирования.
 * Позволяет добавлять, удалять, редактировать поезда и управлять ролями пользователей.
 */
@Controller
public class AdminController {
    private final TrainService trainService;
    private final UserService userService;

    public AdminController(TrainService trainService, UserService userService) {
        this.trainService = trainService;
        this.userService = userService;
    }

    /**
     * Отображает форму добавления нового поезда.
     * Если в модель не передан объект train, создаёт новый.
     *
     * @param model модель данных для представления
     * @return имя шаблона "add-train"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add-form")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("train")) {
            model.addAttribute("train", new Train());
        }
        return "add-train";
    }

    /**
     * Добавляет новый поезд.
     * В случае ошибки сохраняет введённые данные и показывает сообщение об ошибке.
     *
     * @param train данные поезда из формы
     * @param redirectAttributes атрибуты для передачи данных между редиректами
     * @return редирект на главную страницу или обратно к форме при ошибке
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-train")
    public String addTrain(@ModelAttribute Train train,
                           RedirectAttributes redirectAttributes) {
        try {
            Train savedTrain = trainService.createTrain(train);
            redirectAttributes.addFlashAttribute("success", "Поезд успешно добавлен");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("train", train);
            return "redirect:/add-form";
        }
    }

    /**
     * Удаляет поезд по ID и возвращает пользователя к списку с теми же фильтрами.
     * Используется AJAX-формой в таблице.
     *
     * @param id ID поезда
     * @param fromCity текущий фильтр города отправления
     * @param toCity текущий фильтр города прибытия
     * @param departureDate текущая дата отправления
     * @param sortBy поле сортировки
     * @param redirectAttributes атрибуты для передачи параметров через редирект
     * @return редирект на список поездов с сохранением фильтрации
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete-train/{id}")
    public String deleteTrain(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String fromCity,
            @RequestParam(required = false) String toCity,
            @RequestParam(required = false) LocalDate departureDate,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            RedirectAttributes redirectAttributes) {

        trainService.deleteTrain(id);

        redirectAttributes.addAttribute("fromCity", fromCity);
        redirectAttributes.addAttribute("toCity", toCity);
        redirectAttributes.addAttribute("departureDate", departureDate);
        redirectAttributes.addAttribute("sortBy", sortBy);

        return "redirect:/";
    }

    /**
     * Отображает форму редактирования поезда.
     * Загружает поезд по ID и передаёт его в модель.
     *
     * @param id ID поезда
     * @param model модель данных
     * @return имя шаблона "update-train"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/update-form/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Train train = trainService.getTrainById(id);
        model.addAttribute("train", train);
        return "update-train";
    }

    /**
     * Обновляет данные поезда.
     * При ошибке валидации возвращает пользователя к той же форме с сообщением.
     *
     * @param train обновлённые данные поезда
     * @param id ID поезда
     * @param redirectAttributes атрибуты для передачи сообщений после редиректа
     * @return редирект на главную страницу или обратно в форму при ошибке
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update-train/{id}")
    public String updateTrain(@ModelAttribute Train train,
                              @PathVariable("id") Long id,
                              RedirectAttributes redirectAttributes) {
        try {
            train.setId(id);
            trainService.updateTrain(train);
            redirectAttributes.addFlashAttribute("success", "Поезд успешно обновлён");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("train", train);
            return "redirect:/update-form/" + id;
        }
    }

    /**
     * Отображает панель администратора с пользователями.
     * Требует роли ADMIN.
     *
     * @param model модель данных
     * @return имя шаблона "admin-panel"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminForm(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin-panel";
    }

    /**
     * Обновляет роль пользователя.
     * Используется в панели администратора.
     *
     * @param userId ID пользователя
     * @param newRole новая роль ("ROLE_USER" или "ROLE_ADMIN")
     * @param authentication текущая информация об авторизованном пользователе
     * @param redirectAttributes атрибуты для отображения ошибок
     * @return редирект на панель администратора
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/update-role")
    public String updateUserRole(@RequestParam Long userId,
                                 @RequestParam String newRole,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserRole(userId, newRole, authentication);
            return "redirect:/admin";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }
    }
}