package ci.koodysgroup.database.seeds;

import ci.koodysgroup.domains.entities.Role;
import ci.koodysgroup.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleSeeder implements CommandLineRunner {

    @Autowired
    RoleRepository repository;

    private static final List<ci.koodysgroup.domains.entities.Role> roles = List.of(
            new ci.koodysgroup.domains.entities.Role("Administrator","admin"),
            new ci.koodysgroup.domains.entities.Role("Customer","user")
    );

    @Override
    public void run(String... args) throws Exception {
        for(Role role : roles)
        {
            repository.findByAlias(role.getAlias())
                    .orElseGet(() -> repository.save(role));
        }
        System.out.println("Roles successfully inserted ✅");
    }
}
