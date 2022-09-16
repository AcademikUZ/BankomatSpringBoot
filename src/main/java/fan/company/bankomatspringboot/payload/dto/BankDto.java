package fan.company.bankomatspringboot.payload.dto;

import fan.company.bankomatspringboot.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankDto {

    @NotNull
    private String name;

    @NotNull
    private String city;

    @NotNull
    private String street;

}
