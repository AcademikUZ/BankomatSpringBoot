package fan.company.bankomatspringboot.repository;

import fan.company.bankomatspringboot.entity.Role;
import fan.company.bankomatspringboot.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);
}
