package ru.onalex.odashop.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.utils.ServiceUtils;

import static ru.onalex.odashop.services.ImageService.ASSETS;
import static ru.onalex.odashop.utils.ServiceUtils.replaceQuotes;

@Getter
@Setter
@Builder
public class GrupTovDTO {

    private int id;
    private String purl;
    private int nomer;
    private int blok;
    private String rod;
    private String grupName;
    private String normalName;
    private String picPreview;
    private String keywords;
    private String description;
    private String alias;

    public static GrupTovDTO fromEntity(GrupTov grupTov) {
        return GrupTovDTO.builder()
                .id(grupTov.getId())
                .purl(grupTov.getPurl())
                .nomer(grupTov.getNomer())
                .blok(grupTov.getBlok())
                .rod(grupTov.getRod())
                .grupName(grupTov.getGrupName())
                .picPreview(ASSETS + grupTov.getPicPreview())
                .keywords(grupTov.getKeywords())
                .description(grupTov.getDescription())
                .alias(grupTov.getPurl().substring(grupTov.getPurl().lastIndexOf("/") + 1))
                .normalName(replaceQuotes(grupTov.getGrupName()))
                .build();
    }


}
