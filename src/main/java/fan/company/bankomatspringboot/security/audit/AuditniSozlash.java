package fan.company.bankomatspringboot.security.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditniSozlash {

    @Bean
    public AuditorAware<Long> auditProvider(){
        return new KimYozganiniAuditQilish();
    }

}
