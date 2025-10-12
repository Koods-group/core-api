package ci.koodysgroup.repositories;

import ci.koodysgroup.domains.entities.Category;
import ci.koodysgroup.domains.types.LocalizedText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByLibelle(LocalizedText libelle);
}
