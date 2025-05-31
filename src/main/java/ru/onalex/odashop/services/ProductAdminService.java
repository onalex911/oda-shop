package ru.onalex.odashop.services;

import jakarta.validation.Valid;
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
import java.util.ArrayList;
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
//            List<GrupTov> groups = grupTovRepository.findBijou();
            GrupTov currentGroup = grupTovRepository.findById(groupId);
            String groupName = currentGroup.getGrupName();
//            System.out.println(groupName);
//            System.out.println(groupAlias);
            model.addAttribute("products", tovary);
//            model.addAttribute("groups", groups);
            model.addAttribute("group_name", groupName);
            model.addAttribute("group_id", groupId);
//            model.addAttribute("userData", customerService.getUserInfoByUsername(principal.getName()));
            model.addAttribute("title", "Управление товарами");
            model.addAttribute("search_results", false);
//            model.addAttribute("title", groupName + ". ");
            return "adminpanel/products-list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "ОШИБКА! " + e.getMessage());
            return "error";
        }
    }

    public List<Tovar> getProductsByGroupId(int groupId) {
        return tovarRepository.findTovarByGroupId(groupId);
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
            Tovar product = tovarRepository.findTovarById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден: " + id));
            //Todo реализовать проверку на возврат пустого результата и/или ошибки и вывести информацию в лог или пользователю

//            List<GrupTov> groups = grupTovRepository.findBijou();
//            model.addAttribute("groups", groups);
            model.addAttribute("product", product);
            model.addAttribute("title", "Редактирование товара");
            return "adminpanel/products-form";
        } catch (Exception e) {
            model.addAttribute("error_message", e.getMessage());
            return "redirect:/adminpanel";
        }
    }

    public void save(@Valid Tovar product) {
        tovarRepository.save(product);
    }

    public String searchByField(String textToSearch, String fieldToSearch, Model model) {
        List<Tovar> products = new ArrayList<>();
        String errorMessage = "";
        double price = 0.0;
        if(fieldToSearch.startsWith("cena")){
            try {

                price = Double.parseDouble(textToSearch);
                errorMessage = price > 0 ? "" : "Неверный формат цены";

            } catch (NumberFormatException e) {

                errorMessage = "Неверный формат цены";

            }
        }
        if(errorMessage.isEmpty()) {
            switch (fieldToSearch) {
                case "tovName":
                    products = tovarRepository.findByTovNameContainingIgnoreCaseOrderByTovName(textToSearch);
                    break;
                    case "tovName_":
                    products = tovarRepository.findByTovNameStartsWithIgnoreCaseOrderByTovName(textToSearch);
                    break;
                case "dop":
                    products = tovarRepository.findByDopContainingIgnoreCaseOrderByDop(textToSearch);
                    break;
                case "dop_":
                    products = tovarRepository.findByDopStartsWithIgnoreCaseOrderByDop(textToSearch);
                    break;
                case "cena":
                    products = tovarRepository.findByCenaEqualsOrderByCena(price);
                    break;
                    case "cena_ot":
                    products = tovarRepository.findByCenaGreaterThanOrderByCena(price - 0.01);
                    break;
                case "cena_do":
                    products = tovarRepository.findByCenaLessThanOrderByCena(price + 0.01);
                    break;
                case "ceny_ot_do":
                    //искомый текст должен прийти в формате a-b
                    String[] priceParts = textToSearch.split("-");
                    if(priceParts.length == 2) {
                        try {
                            double price1 = Double.parseDouble(priceParts[0]);
                            double price2 = Double.parseDouble(priceParts[1]);
                            //если a и b число, но b < a - ошибка
                            if(price1 < price2) {
                                System.out.println("price1 = "+price1+", price2 = "+price2);
                                products = tovarRepository.findByCenaBetweenOrderByCena(price1 - 0.01, price2 + 0.01);
                            }else{
                                errorMessage = "Первое число меньше второго";
                            }
                        } catch (NumberFormatException e) {
                            //если a или b не число - ошибка
                            errorMessage = "Неверный формат цен";
                        }
                    }else{
                        //если пришло a или a-b-c... - ошибка
                        errorMessage = "Неверный формат цен";
                    }
                    break;
                default:
                    errorMessage = "Неизвестное поле для поиска: " + fieldToSearch;
            }
        }
        if(errorMessage.isEmpty()) {
            model.addAttribute("products", products);
            model.addAttribute("search_results", true);
            model.addAttribute("title", "Результаты поиска");
            return "adminpanel/products-list";
        }
        model.addAttribute("error_message", errorMessage);
        return "adminpanel/index";
    }
}
