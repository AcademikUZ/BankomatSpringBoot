package fan.company.bankomatspringboot.security.audit;


import fan.company.bankomatspringboot.entity.Users;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class KimYozganiniAuditQilish implements AuditorAware<Long> {


    @Override
    public Optional<Long> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(        authentication != null
                && authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")){
            return Optional.of(((Users) authentication.getPrincipal()).getId());
        }
        return Optional.empty();
    }
}
