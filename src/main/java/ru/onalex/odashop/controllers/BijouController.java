package ru.onalex.odashop.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.onalex.odashop.services.*;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.repositories.TovarRepository;

@Controller
@RequestMapping("/catalog")
public class BijouController {
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

    @GetMapping("/bizhuteriya/{alias}")
    public String getBjproducts(
            @PathVariable(name="alias") String alias,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {
        return groupService.getGrupTov(alias, page, size, model);
    }
    @GetMapping("/bizhuteriya/{group-alias}/{prod-alias}")
    public String getBjproduct(
            @PathVariable(name="group-alias") String groupAlias,
            @PathVariable(name="prod-alias") String prodAlias,
            Model model) {
        return productService.getProductPage(groupAlias,prodAlias,model);
    }



}
