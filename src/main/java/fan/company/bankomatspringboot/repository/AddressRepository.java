package fan.company.bankomatspringboot.repository;

import fan.company.bankomatspringboot.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
