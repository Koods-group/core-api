package ci.koodysgroup.repositories;

import ci.koodysgroup.domains.entities.Access;
import ci.koodysgroup.domains.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository  extends JpaRepository<Token , UUID> {
    Optional<Token> findByToken(String token);
}
