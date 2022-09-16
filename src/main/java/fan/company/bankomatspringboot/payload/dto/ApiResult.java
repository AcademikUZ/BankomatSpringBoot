package fan.company.bankomatspringboot.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResult {

    private String message;

    private boolean success;

    private Object object;


    public ApiResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
