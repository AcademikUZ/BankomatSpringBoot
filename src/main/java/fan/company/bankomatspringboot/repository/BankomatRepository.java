package fan.company.bankomatspringboot.repository;

import fan.company.bankomatspringboot.entity.Bankomat;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface BankomatRepository extends JpaRepository<Bankomat, Long> {

    Optional<Bankomat> findByAddress_CityAndAddress_StreetAndOwnerBank_Id(@NotNull String address_city, @NotNull String address_street, Long ownerBank_id);
}
