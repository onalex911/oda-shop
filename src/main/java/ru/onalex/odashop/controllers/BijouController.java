package ru.onalex.odashop.controllers;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.onalex.odashop.services.*;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.repositories.TovarRepository;

@Controller
@RequestMapping("/catalog")
public class BijouController {

    @Value("${spring.data.web.pageable.default-page-size}") private int pageSize;
    private GrupTovRepository grupTovRepository;
    private TovarRepository tovarRepository;
    private GroupService groupService;
    private ProductService productService;

    @Autowired
    public void setGrupTovRepository(GrupTovRepository grupTovRepository) {
        this.grupTovRepository = grupTovRepository;
    }
    @Autowired
    public void setTovarRepository(TovarRepository tovarRepository) {
        this.tovarRepository = tovarRepository;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }


    @GetMapping("/bizhuteriya")
    public String getBjGroups(Model model) {
        return groupService.getGroups(model);
    }

    /**
     * Вывод группы товара
     * @param alias - legacy-псевдоним группы
     * @param page - номер страницы
     * @param size - кол-во позиций на странице
     * @param model - шаблон Thymeleaf
     * @param session - для определения, какой товар уже есть в корзине
     * @return - шаблон, настроенный в сервисе
     */
    @GetMapping("/bizhuteriya/{alias}")
    public String getBjproducts(
            @PathVariable(name="alias") String alias,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size,
            @RequestParam(defaultValue = "undef") String sort,
            Model model, HttpSession session) {
        return groupService.getGrupTov(alias, page, size, sort, model, session);
    }
    @GetMapping("/bizhuteriya/{group-alias}/{prod-alias}")
    public String getBjproduct(
            @PathVariable(name="group-alias") String groupAlias,
            @PathVariable(name="prod-alias") String prodAlias,
            Model model, HttpSession session) {
        return productService.getProductPage(groupAlias,prodAlias,model,session);
    }

    @GetMapping("/bizhuteriya/search")
    public String searchByField(@RequestParam(value = "textToSearch", required = true) String textToSearch,
                                @RequestParam(value = "fieldToSearch", required = true) String fieldToSearch,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "${spring.data.web.pageable.default-page-size}") int size,
                                Model model) {
        return productService.searchByField(textToSearch,fieldToSearch,page,size,model);
    }

}
