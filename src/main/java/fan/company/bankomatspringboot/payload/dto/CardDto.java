package fan.company.bankomatspringboot.payload.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDto {

    @NotNull
    @Size(min=16, max=16)
    private Long maxsusRaqam;

    @NotNull
    @Size(min=3, max=3)
    private Long CVV;

    @NotNull
    @Size(min=4, max=4)
    private Long pincode;

    @NotNull
    private String fullNameOwner;

    @NotNull
    private LocalDate expireDate;

    @NotNull
    private Long bankId;

    @NotNull
    private Long plasticTypeId;

    private boolean active;

    @NotNull
    private String fullName;

    @NotNull
    private String username;

    @NotNull
    private String password;


}
