package fan.company.bankomatspringboot.payload.projection;

import fan.company.bankomatspringboot.entity.Bank;
import org.springframework.data.rest.core.config.Projection;

@Projection(types = Bank.class)
public interface CustomBank {

    public Long getId();

    public String getName();



}
