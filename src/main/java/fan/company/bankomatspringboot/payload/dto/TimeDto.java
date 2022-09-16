package fan.company.bankomatspringboot.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeDto {

    private Timestamp start;

    private Timestamp end;

}
