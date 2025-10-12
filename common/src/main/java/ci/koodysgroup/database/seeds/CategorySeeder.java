package ci.koodysgroup.database.seeds;

import ci.koodysgroup.domains.entities.Category;
import ci.koodysgroup.domains.types.LocalizedText;
import ci.koodysgroup.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategorySeeder implements CommandLineRunner  {

    @Autowired
    CategoryRepository repository;

    private static final List<Category> categories = List.of(
            new Category(
                    new LocalizedText("Comédie", "Comedy"),
                    new LocalizedText("Films légers et amusants", "Light and funny movies")
            ),
            new Category(
                    new LocalizedText("Action", "Action"),
                    new LocalizedText("Scènes palpitantes et aventures", "Thrilling scenes and adventures")
            ),
            new Category(
                    new LocalizedText("Science fiction", "Science fiction"),
                    new LocalizedText("Histoires futuristes ou imaginaires", "Futuristic or imaginative stories")
            ),
            new Category(
                    new LocalizedText("Documentaire", "Documentary"),
                    new LocalizedText("Récits réels et informatifs", "Real and informative stories")
            ),
            new Category(
                    new LocalizedText("Drame", "Drama"),
                    new LocalizedText("Émotions fortes et histoires sérieuses", "Strong emotions and serious stories")
            ),
            new Category(
                    new LocalizedText("Humour", "Humour"),
                    new LocalizedText("Rires et situations comiques", "Laughs and funny situations")
            )
    );

    @Override
    public void run(String... args) throws Exception {
        for(Category category : categories)
        {
            repository.findByLibelle(category.getLibelle())
                    .orElseGet(() -> repository.save(category));
        }
        System.out.println("Categories successfully inserted ✅");
    }

}
