package ru.onalex.odashop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.repositories.BJGroupRepository;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.repositories.TovarRepository;

import java.util.List;

@Service
public class ProductService {

    private TovarRepository tovarRepository;
    private BJGroupRepository bjGroupRepository;

    @Autowired
    public void setTovarRepository(TovarRepository tovarRepository) {
        this.tovarRepository = tovarRepository;
    }

    @Autowired
    public void setBjGroupRepository(BJGroupRepository bjGroupRepository) {
        this.bjGroupRepository = bjGroupRepository;
    }

    public String getProductPage(String groupAlias, String prodAlias, Model model){
        try {
            int prodId = getLastNumber(prodAlias);
            Tovar product = tovarRepository.findExistTovarByCode(prodId);
            String groupName = bjGroupRepository.findByAlias(groupAlias).getGroupNameRu();
            System.out.println(groupName);
            System.out.println(prodId);
            model.addAttribute("product", product);
            model.addAttribute("group_name", groupName);

            return "single-product";
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    public static int getLastNumber(String str) throws Exception {
        int lastUnderscore = str.lastIndexOf('_'); // Находим последний '_'
        if (lastUnderscore != -1) {
            String numberStr = str.substring(lastUnderscore + 1); // Берём подстроку после '_'
            try {
                int number = Integer.parseInt(numberStr);
                return number;
            } catch (NumberFormatException e) {
                System.out.println("Последняя часть не является числом!");
            }
        } else {
            throw new Exception("Нет символа '_' в строке!");
        }
        return 0;
    }
}
