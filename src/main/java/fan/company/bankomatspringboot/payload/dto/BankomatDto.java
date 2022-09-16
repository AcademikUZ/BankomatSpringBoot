package fan.company.bankomatspringboot.payload.dto;

import fan.company.bankomatspringboot.entity.PlasticType;
import fan.company.bankomatspringboot.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankomatDto {

    @NotNull
    private List<PlasticType> plasticTypeList;

    @NotNull
    private Double yechiladiganMaxPulMiqdori;

    @NotNull
    private Long ownerBankId;

    @NotNull
    private Double commissionMiqdoriForOwner;

    @NotNull
    private Double commissionMiqdoriForOther;

    @NotNull
    private Double forSignalMaxMiqdorPul;

    @NotNull
    private Double forSignalMinMiqdorPul;

    @NotNull
    private String city;

    @NotNull
    private String street;

    private Long mingSomCount;

    private Long beshmingSomCount;

    private Long onmingSomCount;

    private Long ellikmingSomCount;

    private Long yuzmingSomCount;

    private Long ikkimingSomCount;

    private Long masulXodimId;
}
