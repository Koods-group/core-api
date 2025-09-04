package ci.koodysgroup.repositories;

import ci.koodysgroup.domains.entities.Access;
import ci.koodysgroup.domains.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessRepository extends JpaRepository<Access, UUID> {
    Optional<Access> findByLoginAndCountry(String login, Country country);
}
