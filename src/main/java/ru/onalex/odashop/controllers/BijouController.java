package ru.onalex.odashop.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.repositories.BJGroupRepository;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.repositories.TovarRepository;

import java.util.List;

@Controller
@RequestMapping("/catalog/bizhuteriya")
public class BijouController {
    private GrupTovRepository grupTovRepository;
    private TovarRepository tovarRepository;
    private BJGroupRepository bjGroupRepository;

    @Autowired
    public void setGrupTovRepository(GrupTovRepository grupTovRepository) {
        this.grupTovRepository = grupTovRepository;
    }
    @Autowired
    public void setTovarRepository(TovarRepository tovarRepository) {
        this.tovarRepository = tovarRepository;
    }

    @Autowired
    public void setBjGroupRepository(BJGroupRepository bjGroupRepository) {
        this.bjGroupRepository = bjGroupRepository;
    }


    @GetMapping("")
    public String getBjGroups(Model model) {
        List<GrupTov> groups = grupTovRepository.findBijou();
        model.addAttribute("groups",groups);
        System.out.println(groups.size());
        return "bj_groups";
    }
    @GetMapping("/{alias}")
    public String getBjproducts(
            @PathVariable(name="alias") String alias, Model model) {
        List<Tovar> products = tovarRepository.findTovarByAlias(alias);
        String groupName = bjGroupRepository.findByAlias(alias).getGroupNameRu();
        model.addAttribute("products",products);
        model.addAttribute("group_name",groupName);
        System.out.println(products.size());
        return "bj_products";
    }
}
