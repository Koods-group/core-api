package ci.koodysgroup.repositories;

import ci.koodysgroup.domains.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {
    Optional<Country> findByAlias(String alias);
}
