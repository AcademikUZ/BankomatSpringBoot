package fan.company.bankomatspringboot.repository;

import fan.company.bankomatspringboot.entity.Amal;
import fan.company.bankomatspringboot.entity.AuditBankomat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface AuditBankomatRepository extends JpaRepository<AuditBankomat, Long> {

    Optional<AuditBankomat> findByAmal(Amal amal);

    List<AuditBankomat> findByCreateAtBetween(Timestamp start, Timestamp end);


}
