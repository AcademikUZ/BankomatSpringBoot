package fan.company.bankomatspringboot.repository;

import fan.company.bankomatspringboot.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByMaxsusRaqam(@NotNull @Size(min = 16, max = 16) Long maxsusRaqam);

}
