package ci.koodysgroup.database.seeds;

import ci.koodysgroup.domains.entities.Country;
import ci.koodysgroup.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountrySeeder implements CommandLineRunner {

    @Autowired
    CountryRepository repository;

    private static final List<Country> countries = List.of(
            new Country("Côte D'Ivoire","CI","https://flagcdn.com/w320/ci.png","225",10)
    );

    @Override
    public void run(String... args) throws Exception {
        for(Country country : countries)
        {
            repository.findByAlias(country.getAlias())
                    .orElseGet(() -> repository.save(country));
        }
        System.out.println("Countries successfully inserted ✅");
    }
}
