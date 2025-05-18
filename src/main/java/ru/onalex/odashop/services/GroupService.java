package ru.onalex.odashop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import ru.onalex.odashop.dtos.GrupTovDTO;
import ru.onalex.odashop.dtos.TovarDTO;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.entities.Tovar;
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

    @Autowired
    public GroupService(GrupTovRepository grupTovRepository, TovarRepository tovarRepository, ImageService imageService) {
        this.grupTovRepository = grupTovRepository;
        this.tovarRepository = tovarRepository;
        this.imageService = imageService;
    }

    public List<GrupTovDTO> getGroupsDto() {
        return grupTovRepository.findBijou()
                .stream().map(GrupTovDTO::fromEntity).collect(Collectors.toList());
    }

    public String getGroups(Model model){
        model.addAttribute("groups",getGroupsDto());
        model.addAttribute("title","Бижутерия");
        return "groups-page";
    }

    public String getGrupTov(String alias, int page, int size, Model model) {
        try {
            Page<Tovar> tovarPage = tovarRepository.findTovarByAlias(alias, PageRequest.of(page, size));
//            System.out.println("tovar page" + tovarPage.getTotalElements());

            Page<TovarDTO> products = tovarPage.map(TovarDTO::fromEntity);
//            System.out.println("products page" + products.getTotalElements());

            products.getContent().forEach(product -> {
                System.out.println("pic from repo: " + product.getPicBig());
                product.setRealPicBig(imageService.getImagePath(product.getPicBig()));
            });

//            for(TovarDTO t : products){
//                System.out.println("real pic: " + t.getRealPicBig());
//            }

            GrupTovDTO grupTovDTO = fromEntity(grupTovRepository.findByAlias(alias));

            model.addAttribute("products", products);
            model.addAttribute("group_name", grupTovDTO.getNormalName());
            model.addAttribute("totalItems", products.getTotalElements());
            model.addAttribute("totalPages", products.getTotalPages());
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("title", grupTovDTO.getNormalName() + ". ");

            return "single-group";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}
