package ru.onalex.odashop.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.onalex.odashop.entities.GrupTov;
import ru.onalex.odashop.entities.Tovar;

import static ru.onalex.odashop.utils.ServiceUtils.replaceQuotes;

@Getter
@Setter
@Builder
public class TovarDTO {
    private int code;
    private String purl;
    private int nomer;
    private int grupTov;
    private String tovName;
    private String normalTovName;
    private String picPreview;
    private String picBig;
    private int ostatok;
    private double cena;
    private double cena1;
    private double ves;
//    private int blok;
//    private int prosmotru;
//    private String comment;
//    private String comment1;

    public static TovarDTO fromEntity(Tovar tovar) {
        return TovarDTO.builder()
                .code(tovar.getCode())
                .purl(tovar.getPurl())
                .nomer(tovar.getNomer())
                .grupTov(tovar.getGrupTov())
                .tovName(tovar.getTovName())
                .normalTovName(replaceQuotes(tovar.getTovName()))
                .picPreview(tovar.getPicPreview())
                .picBig(tovar.getPicBig())
                .ostatok(tovar.getOstatok())
                .cena(tovar.getCena())
                .cena1(tovar.getCena1())
                .ves(tovar.getVes())
//                .blok(tovar.getBlok())
                .build();
    }

}
