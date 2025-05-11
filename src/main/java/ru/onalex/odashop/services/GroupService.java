package ru.onalex.odashop.services;

import org.springframework.beans.factory.annotation.Autowired;
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
//    private GrupTovDTO grupTovDTO;

    public GroupService(GrupTovRepository grupTovRepository, TovarRepository tovarRepository) {
        this.grupTovRepository = grupTovRepository;
        this.tovarRepository = tovarRepository;
    }

    public String getGroups(Model model){
        List<GrupTov> groups = grupTovRepository.findBijou();
        model.addAttribute("groups",groups);
//        System.out.println(groups.size());
        return "groups-page";
    }

    public String getGrupTov(String alias, Model model) {
        List<TovarDTO> products = tovarRepository.findTovarByAlias(alias)
                .stream()
                .map(TovarDTO::fromEntity).collect(Collectors.toList());
        GrupTovDTO grupTovDTO = fromEntity(grupTovRepository.findByAlias(alias));
        String groupName = grupTovDTO.getNormalName();
        model.addAttribute("products",products);
        model.addAttribute("group_name",groupName);
        System.out.println(products.size());
        return "single-group";
    }
}
