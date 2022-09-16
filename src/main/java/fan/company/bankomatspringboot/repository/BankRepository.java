package fan.company.bankomatspringboot.repository;

import fan.company.bankomatspringboot.entity.Bank;
import fan.company.bankomatspringboot.payload.projection.CustomBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@RepositoryRestResource(path = "bank", collectionResourceRel = "list", excerptProjection = CustomBank.class)
public interface BankRepository extends JpaRepository<Bank, Long> {

    Optional<Bank> findByName(@NotNull String name);
}
