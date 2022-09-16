package fan.company.bankomatspringboot.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayDto {

    private Long fromCardId;

    private Long toCardId;

    private Long bankomatId;

    private Double transaksiyaPulMiqdori;

    private Long pinCode;

    private Long mingSomCount;

    private Long beshmingSomCount;

    private Long onmingSomCount;

    private Long ellikmingSomCount;

    private Long yuzmingSomCount;

    private Long ikkimingSomCount;


}
