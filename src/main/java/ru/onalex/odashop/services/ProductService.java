package ru.onalex.odashop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.onalex.odashop.dtos.GrupTovDTO;
import ru.onalex.odashop.dtos.TovarDTO;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.repositories.TovarRepository;

import java.util.List;

import static ru.onalex.odashop.utils.ServiceUtils.replaceQuotes;

@Service
public class ProductService {

    private GrupTovRepository grupTovRepository;
    private TovarRepository tovarRepository;

    @Autowired
    public void setTovarRepository(TovarRepository tovarRepository) {
        this.tovarRepository = tovarRepository;
    }
    @Autowired
    public void setGrupTovRepository(GrupTovRepository grupTovRepository) {
        this.grupTovRepository = grupTovRepository;
    }

    public String getProductPage(String groupAlias, String prodAlias, Model model){
        try {
            int prodId = getLastNumber(prodAlias);
            TovarDTO tovarDTO = TovarDTO.fromEntity(tovarRepository.findExistTovarByCode(prodId));
            GrupTovDTO grupTovDTO = GrupTovDTO.fromEntity(grupTovRepository.findByAlias(groupAlias));
            String groupName = grupTovDTO.getNormalName();
//            String groupName = grupTovRepository.findByAlias(groupAlias).getGrupName();
//            System.out.println(groupName);
//            System.out.println(groupAlias);
            model.addAttribute("product", tovarDTO);
            model.addAttribute("group_name", groupName);
            model.addAttribute("group_alias", groupAlias);
            model.addAttribute("title", tovarDTO.getNormalTovName() + ". " + groupName + ". ");
            return "single-product";
        }catch (Exception e){
            model.addAttribute("errorMessage", "ОШИБКА! " + e.getMessage());
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

    public long count(){
        return tovarRepository.count();
    }

    public List<Tovar> findAll(){
        return tovarRepository.findAll();
    }
}
