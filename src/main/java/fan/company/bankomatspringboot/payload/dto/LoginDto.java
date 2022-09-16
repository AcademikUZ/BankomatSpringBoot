package fan.company.bankomatspringboot.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;


}
