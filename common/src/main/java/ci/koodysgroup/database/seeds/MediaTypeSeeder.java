package ci.koodysgroup.database.seeds;

import ci.koodysgroup.domains.entities.Category;
import ci.koodysgroup.domains.entities.MediaType;
import ci.koodysgroup.domains.types.LocalizedText;
import ci.koodysgroup.repositories.MediaTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MediaTypeSeeder  implements CommandLineRunner {

    @Autowired
    MediaTypeRepository repository;

    private static final List<MediaType> types = List.of(
            new MediaType(new LocalizedText("Films","Movies")),
            new MediaType(new LocalizedText("Series" , "Series"))
    );

    @Override
    public void run(String... args) throws Exception {
        for(MediaType type : types)
        {
            repository.findByLibelle(type.getLibelle())
                    .orElseGet(() -> repository.save(type));
        }
        System.out.println("Media Types successfully inserted ✅");
    }
}
