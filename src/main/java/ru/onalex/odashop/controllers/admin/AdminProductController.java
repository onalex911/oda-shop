package ru.onalex.odashop.controllers.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.models.MyResponse;
import ru.onalex.odashop.services.CustomerService;
import ru.onalex.odashop.services.GroupService;
import ru.onalex.odashop.services.ProductAdminService;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

//import static ru.onalex.odashop.controllers.GlobalControllerAdvice.uploadDir;

@Controller
@RequestMapping("/adminpanel/products")
public class AdminProductController {
//    @Value("${app.upload.dir}")
//    public static String uploadDir;
    public static int DEFAULT_GROUP = 1418;

    private final CustomerService customerService;
    private final GroupService groupService;
    private final ProductAdminService productAdminService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public AdminProductController(GroupService grupTovService, ProductAdminService productAdminService, CustomerService customerService, ResourceLoader resourceLoader) {
        this.groupService = grupTovService;
        this.productAdminService = productAdminService;
        this.customerService = customerService;
        this.resourceLoader = resourceLoader;
    }

    /**
     * Вывод списка товаров категории, выбранной по-умолчанию
     */
    @GetMapping()
    public String listProductsDef(Principal principal, Model model) {
        return listProducts(DEFAULT_GROUP, principal, model);
    }

    @GetMapping("/{id}")
    public String listProducts(@PathVariable int id, Principal principal, Model model) {

       return productAdminService.getProductsByGroupId(id,model,principal);
//        model.addAttribute("categories", groupService.getGroupsAll());
//        model.addAttribute("userData", customerService.getUserInfoByUsername(principal.getName()));
//        model.addAttribute("title", "Управление товарами");
//        return "adminpanel/products-list";

    }

    /**
     * Форма создания новой категории
     */
    @GetMapping("/create")
    public String createCategoryForm(Principal principal, Model model) {
        model.addAttribute("category", new GrupTov());
        model.addAttribute("userData", customerService.getUserInfoByUsername(principal.getName()));
        model.addAttribute("title", "Создание категории");
        return "adminpanel/products-form";
    }

    /**
     * Обработка формы создания категории
     */
    @PostMapping("/create")
    public String createCategory(@Valid @ModelAttribute("category") GrupTov category,
                                 BindingResult bindingResult,
                                 Model model,
                                 Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("userData", customerService.getUserInfoByUsername(principal.getName()));
            model.addAttribute("title", "Создание категории");
            return "adminpanel/products-form";
        }

        groupService.save(category);
        return "redirect:/adminpanel/products";
    }

    /**
     * Форма редактирования категории
     */
    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable int id, Model model, Principal principal) {
        GrupTov category = groupService.findById(id);
        //Todo реализовать проверку на возврат пустого результата и/или ошибки и вывести информацию в лог или пользователю
//                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена: " + id));

        model.addAttribute("userData", customerService.getUserInfoByUsername(principal.getName()));
        model.addAttribute("category", category);
        model.addAttribute("title", "Редактирование категории");
        return "adminpanel/products-form";
    }

    /**
     * Обработка формы редактирования категории
     */
    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable int id,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               @Valid @ModelAttribute("category") GrupTov category,
                               BindingResult bindingResult,
                               Model model,
                               Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", "Редактирование товара");
            model.addAttribute("userData", customerService.getUserInfoByUsername(principal.getName()));
            return "adminpanel/products-form";
        }
        //сохраняем приложенное изображение (если оно было вставлено в форму)
        if (imageFile != null && !imageFile.isEmpty()) {
            // Генерируем уникальное имя файла
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

            // Получаем путь к ресурсам
            Resource resource = resourceLoader.getResource("classpath:static/assets/pic/tovar/gruptov/");
            File uploadDirFile = resource.getFile();

            // Создаем директорию, если она не существует
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // Сохраняем файл
            File dest = new File(uploadDirFile, fileName);
            imageFile.transferTo(dest);

            // Путь к файлу, который будет сохранен в базе данных
            String dbFilePath = "pic/tovar/gruptov/" + fileName;
            category.setPicPreview(dbFilePath);
        }

        category.setId(id); // Убедимся, что ID установлен правильно
        groupService.save(category);
        return "redirect:/adminpanel/categories";
    }

    @PostMapping("/block/{id}/{status}")
    @ResponseBody
    public MyResponse blockCategory(@PathVariable int id,
                                  @PathVariable int status) {
//        System.out.println("id="+id+" status="+status);
        return productAdminService.setActivity(id,status);
    }
    /**
     * Удаление категории (НЕ ИСПОЛЬЗУЕТСЯ В ИНТЕРФЕЙСЕ. ВМЕСТО НЕГО ИСПОЛЬЗУЕТСЯ ДЕАКТИВАЦИЯ)
     */
    /*@GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        grupService.deleteById(id);
        return "redirect:/adminpanel/categories";
    }*/
}
