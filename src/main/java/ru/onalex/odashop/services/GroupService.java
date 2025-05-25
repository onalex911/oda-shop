package ru.onalex.odashop.services;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.onalex.odashop.dtos.CartItemDTO;
import ru.onalex.odashop.dtos.GrupTovDTO;
import ru.onalex.odashop.dtos.TovarDTO;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.entities.Tovar;
import ru.onalex.odashop.models.CartInfo;
import ru.onalex.odashop.models.MyResponse;
import ru.onalex.odashop.repositories.GrupTovRepository;
import ru.onalex.odashop.repositories.TovarRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.onalex.odashop.dtos.GrupTovDTO.fromEntity;

@Service
public class GroupService {
    private final GrupTovRepository grupTovRepository;
    private final TovarRepository tovarRepository;
    private final ImageService imageService;
    private final CartService cartService;

    @Autowired
    public GroupService(GrupTovRepository grupTovRepository, TovarRepository tovarRepository, ImageService imageService,  CartService cartService) {
        this.grupTovRepository = grupTovRepository;
        this.tovarRepository = tovarRepository;
        this.imageService = imageService;
        this.cartService = cartService;
    }

    public List<GrupTov> getGroupsAll() {
        return grupTovRepository.findBijou();
    }
    public List<GrupTovDTO> getGroupsDto() {
        return grupTovRepository.findBijouActive()
                .stream().map(GrupTovDTO::fromEntity).collect(Collectors.toList());
    }

    public String getGroups(Model model){
        model.addAttribute("groups",getGroupsDto());
        model.addAttribute("title","");
        return "groups-page";
    }

    public String getGrupTov(String alias, int page, int size, String sortType, Model model, HttpSession session) {
        String sortField = "";
        String sortDirection = "";
        String errorMessage = "";

        if(sortType.startsWith("price")){
            sortField = "cena";
            sortDirection = sortType.equals("price_up") ? "asc" : "desc";
        }else if(sortType.startsWith("rem")){
            sortField = "ostatok";
            sortDirection = sortType.equals("rem_up") ? "asc" : "desc";
        }else if(sortType.equals("dop")) {
            sortField = "dop";
            sortDirection = "asc";
        }else if(sortType.equals("undef")){
            sortField = "nomer";
            sortDirection = "asc";
        }else{
            errorMessage = "Неверный тип сортировки!";
        }
        if(errorMessage.isEmpty()) {
//            System.out.println("field: "+sortField+", dir: "+sortDirection);
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
            String sortParam = "&sort=" + sortType;

            try {
                int pageSize = size == 0 ? 1000 : size; //ToDo: найти лучшее решение для вычисления максимального кол-ва товара на странице
                Page<Tovar> tovarPage = tovarRepository.findTovarByAlias(alias, PageRequest.of(page, pageSize, sort));
                Page<TovarDTO> products = tovarPage.map(TovarDTO::fromEntity);
                products.getContent().forEach(product -> {
//                    System.out.println("pic from repo: " + product.getPicBig());
                    product.setRealPicBig(imageService.getImagePath(product.getPicBig()));
                    int quantity = cartService.getQuantity(product.getId(), session);
//                    System.out.println("quantity of "+product.getId()+" is "+quantity);
                    product.setInCart(quantity);
                });

                GrupTovDTO grupTovDTO = fromEntity(grupTovRepository.findByAlias(alias));

                model.addAttribute("products", products);
                model.addAttribute("group_name", grupTovDTO.getNormalName());
                model.addAttribute("totalItems", products.getTotalElements());
                model.addAttribute("totalPages", products.getTotalPages());
                model.addAttribute("currentPage", page);
                model.addAttribute("pageSize", size);
                model.addAttribute("title", grupTovDTO.getNormalName());
                model.addAttribute("sortparam", sortType);

                return "single-group";
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "search-empty";
    }

    public GrupTov findById(int id) {
        return grupTovRepository.findById(id);
    }

    public void save(GrupTov grupTov) {
        grupTovRepository.save(grupTov);
    }

    public MyResponse setActivity(int id, int status) {
        if (id <= 0) {
            return MyResponse.error("Некорректный ID");
        }

        if (status != 0 && status != 1) {
            return MyResponse.error("Статус должен быть 0 или 1");
        }

        try {
//            int updatedRows = grupTovRepository.setBlokStatus(id, status);
            grupTovRepository.setBlokStatus(id, status);

//            if (updatedRows == 0) {
//                return MyResponse.error("Категория с ID " + id + " не найдена");
//            }

            return MyResponse.success("Статус успешно изменен");
        } catch (Exception e) {
//            log.error("Ошибка при изменении статуса блокировки категории: {}", e.getMessage(), e);
            return MyResponse.error(e.getMessage());
        }
    }
}
