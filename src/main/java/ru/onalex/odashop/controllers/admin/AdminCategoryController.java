package ru.onalex.odashop.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.services.CustomerService;
import ru.onalex.odashop.services.GroupService;

import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/adminpanel/categories")
public class AdminCategoryController {
    private final CustomerService customerService;
    private final GroupService grupTovService;

    @Autowired
    public AdminCategoryController(GroupService grupTovService, CustomerService customerService) {
        this.grupTovService = grupTovService;
        this.customerService = customerService;
    }

    /**
     * Вывод списка всех категорий
     */
    @GetMapping
    public String listCategories(Principal principal, Model model) {
        model.addAttribute("categories", grupTovService.getGroupsDto());
        model.addAttribute("userData", customerService.getUserInfoByUsername(principal.getName()));
        model.addAttribute("title", "Управление группами");
        return "adminpanel/categories-list";
//        return "adminpanel/index";
    }

    /**
     * Форма создания новой категории
     */
//    @GetMapping("/create")
//    public String createCategoryForm(Model model) {
//        model.addAttribute("category", new GrupTov());
//        model.addAttribute("title", "Создание категории");
//        return "admin/categories/form";
//    }
//
//    /**
//     * Обработка формы создания категории
//     */
//    @PostMapping("/create")
//    public String createCategory(@Valid @ModelAttribute("category") GrupTov category,
//                                 BindingResult bindingResult,
//                                 Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("title", "Создание категории");
//            return "admin/categories/form";
//        }
//
//        grupTovService.save(category);
//        return "redirect:/adminpanel/categories";
//    }
//
//    /**
//     * Форма редактирования категории
//     */
//    @GetMapping("/edit/{id}")
//    public String editCategoryForm(@PathVariable Long id, Model model) {
//        GrupTov category = grupTovService.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена: " + id));
//
//        model.addAttribute("category", category);
//        model.addAttribute("title", "Редактирование категории");
//        return "admin/categories/form";
//    }
//
//    /**
//     * Обработка формы редактирования категории
//     */
//    @PostMapping("/edit/{id}")
//    public String editCategory(@PathVariable Long id,
//                               @Valid @ModelAttribute("category") GrupTov category,
//                               BindingResult bindingResult,
//                               Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("title", "Редактирование категории");
//            return "admin/categories/form";
//        }
//
//        category.setId(id); // Убедимся, что ID установлен правильно
//        grupTovService.save(category);
//        return "redirect:/adminpanel/categories";
//    }
//
//    /**
//     * Удаление категории
//     */
//    @GetMapping("/delete/{id}")
//    public String deleteCategory(@PathVariable Long id) {
//        grupTovService.deleteById(id);
//        return "redirect:/adminpanel/categories";
//    }
}
