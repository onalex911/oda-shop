package ru.onalex.odashop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.onalex.odashop.dtos.GrupTovDTO;
import ru.onalex.odashop.dtos.TovarDTO;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.models.MyResponse;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.repositories.TovarRepository;

import java.security.Principal;
import java.util.List;

@Service
public class ProductAdminService {
    private final GrupTovRepository grupTovRepository;
    private final TovarRepository tovarRepository;
//    private final ImageService imageService;

    @Autowired
    public ProductAdminService(GrupTovRepository grupTovRepository, TovarRepository tovarRepository) {
        this.grupTovRepository = grupTovRepository;
        this.tovarRepository = tovarRepository;
//        this.imageService = imageService;
    }

    public String getProductsByGroupId(int groupId, Model model) {
        try {
            List<Tovar> tovary = tovarRepository.findTovarByGroupId(groupId);
            List<GrupTov> groups = grupTovRepository.findBijou();
            GrupTov currentGroup = grupTovRepository.findById(groupId);
            String groupName = currentGroup.getGrupName();
//            System.out.println(groupName);
//            System.out.println(groupAlias);
            model.addAttribute("products", tovary);
            model.addAttribute("groups", groups);
            model.addAttribute("group_name", groupName);
            model.addAttribute("group_id", groupId);
//            model.addAttribute("userData", customerService.getUserInfoByUsername(principal.getName()));
            model.addAttribute("title", "Управление товарами");
//            model.addAttribute("title", groupName + ". ");
            return "adminpanel/products-list";
        }catch (Exception e){
            model.addAttribute("errorMessage", "ОШИБКА! " + e.getMessage());
            return "error";
        }
    }

    public MyResponse setActivity(int id, int status) {
        if (id <= 0) {
            return MyResponse.error("Некорректный ID");
        }

        if (status != 0 && status != 1) {
            return MyResponse.error("Статус должен быть 0 или 1");
        }

        try {
            tovarRepository.setBlokStatus(id, status);

            return MyResponse.success("Статус успешно изменен");
        } catch (Exception e) {
//            log.error("Ошибка при изменении статуса блокировки категории: {}", e.getMessage(), e);
            return MyResponse.error(e.getMessage());
        }
    }

    public String editProductForm(int id, Model model) {
        try {
            Tovar product = tovarRepository.findTovarById(id);
            //Todo реализовать проверку на возврат пустого результата и/или ошибки и вывести информацию в лог или пользователю
//                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена: " + id));

            List<GrupTov> groups = grupTovRepository.findBijou();
            model.addAttribute("groups", groups);
            model.addAttribute("product", product);
            model.addAttribute("title", "Редактирование товара");
            return "adminpanel/products-form";
        }catch (Exception e){
            model.addAttribute("error_message",e.getMessage());
            return "redirect:/adminpanel";
        }
    }
}
