package ru.onalex.odashop.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.onalex.odashop.dtos.GrupTovDTO;
import ru.onalex.odashop.dtos.TovarDTO;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.repositories.TovarRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.onalex.odashop.dtos.TovarDTO.fromEntity;
import static ru.onalex.odashop.utils.ServiceUtils.replaceQuotes;

@Service
public class ProductService {

//    private GrupTovRepository grupTovRepository;
//    private TovarRepository tovarRepository;

//    @Autowired
//    public void setTovarRepository(TovarRepository tovarRepository) {
//        this.tovarRepository = tovarRepository;
//    }
//    @Autowired
//    public void setGrupTovRepository(GrupTovRepository grupTovRepository) {
//        this.grupTovRepository = grupTovRepository;
//    }
    private final GrupTovRepository grupTovRepository;
    private final TovarRepository tovarRepository;
    private final ImageService imageService;
    private final CartService cartService;

    @Autowired
    public ProductService(GrupTovRepository grupTovRepository, TovarRepository tovarRepository, ImageService imageService,CartService cartService) {
        this.grupTovRepository = grupTovRepository;
        this.tovarRepository = tovarRepository;
        this.imageService = imageService;
        this.cartService = cartService;
    }


    public String getProductPage(String groupAlias, String prodAlias, Model model, HttpSession session) {
        try {
            int prodId = getLastNumber(prodAlias);
            Tovar tovar = tovarRepository.findExistTovarByCode(prodId);
            TovarDTO tovarDTO = fromEntity(tovar);
            tovarDTO.setRealPicBig(imageService.getImagePath(tovar.getPicBig()));
            GrupTovDTO grupTovDTO = GrupTovDTO.fromEntity(grupTovRepository.findByAlias(groupAlias));
            String groupName = grupTovDTO.getNormalName();
//            String groupName = grupTovRepository.findByAlias(groupAlias).getGrupName();
//            System.out.println(groupName);
//            System.out.println(groupAlias);
            int quantity = cartService.getQuantity(tovarDTO.getId(), session);
//                    System.out.println("quantity of "+product.getId()+" is "+quantity);
            tovarDTO.setInCart(quantity);
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

    public String searchByField(String textToSearch, String fieldToSearch, int page, int size, Model model) {
        Page<Tovar> products = null;
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
                    products = tovarRepository.findByTovNameContainingIgnoreCaseOrderByTovNamePub(textToSearch.toUpperCase(), PageRequest.of(page, size));
                    break;
                case "tovName_":
                    products = tovarRepository.findByTovNameStartsWithIgnoreCaseOrderByTovNamePub(textToSearch.toUpperCase(), PageRequest.of(page, size));
                    break;
                case "dop":
                    products = tovarRepository.findByDopContainingIgnoreCaseOrderByDopPub(textToSearch.toUpperCase(), PageRequest.of(page, size));
                    break;
                case "dop_":
                    products = tovarRepository.findByDopStartsWithIgnoreCaseOrderByDopPub(textToSearch.toUpperCase(), PageRequest.of(page, size));
                    break;
                case "cena":
                    products = tovarRepository.findByCenaEqualsOrderByCenaPub(price, PageRequest.of(page, size));
                    break;
                case "cena_ot":
                    products = tovarRepository.findByCenaGreaterThanOrderByCenaPub(price - 0.01, PageRequest.of(page, size));
                    break;
                case "cena_do":
                    products = tovarRepository.findByCenaLessThanOrderByCenaPub(price + 0.01, PageRequest.of(page, size));
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
                                products = tovarRepository.findByCenaBetweenOrderByCenaPub(price1 - 0.01, price2 + 0.01, PageRequest.of(page, size));
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
            Page<TovarDTO> productsDTO = products.map(TovarDTO::fromEntity);
            productsDTO.getContent().forEach(product -> {
                System.out.println("pic from repo: " + product.getPicPreview());
                product.setRealPreview(imageService.getImagePath(product.getPicPreview()));
            });
            model.addAttribute("products", productsDTO);
            model.addAttribute("title", "Результаты поиска");
            model.addAttribute("totalItems", productsDTO.getTotalElements());
            model.addAttribute("totalPages", productsDTO.getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("textToSearch", textToSearch);
            model.addAttribute("fieldToSearch", fieldToSearch);
            model.addAttribute("pageSize", size);
            return "search";
        }
        model.addAttribute("error_message", errorMessage);
        return "search-empty";
    }
}
