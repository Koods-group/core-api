package ci.koodysgroup.features.movie.handlers;

import ci.koodysgroup.domains.entities.Category;
import ci.koodysgroup.domains.entities.Content;
import ci.koodysgroup.domains.entities.Movie;
import ci.koodysgroup.features.movie.commands.CreateMovieCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.CategoryRepository;
import ci.koodysgroup.repositories.ContentRepository;
import ci.koodysgroup.repositories.MovieRepository;
import ci.koodysgroup.utils.response.CommandResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CreateMovieHandler implements CommandHandler<CreateMovieCommand , CommandResponse<String>> {

    private final CategoryRepository categoryRepository;
    private final ContentRepository contentRepository;
    private final MovieRepository repository;


    @Override
    public CommandResponse<String> handler(CreateMovieCommand command){
        try{

            Content content = contentRepository.findById(command.getContentId())
                    .orElseThrow(()-> new NoSuchElementException("The selected content type does not appear to exist in the system ."));

            Category category = categoryRepository.findById(command.getCategoryId())
                    .orElseThrow(() -> new NoSuchElementException("The selected category type does not appear to exist in the system ."));


            Movie movie = new Movie(category,content,command.getDuration(),command.getDirector(),"");
            repository.save(movie);

            return CommandResponse.success("Recorded movie","Recorded movie");

        } catch (Exception e) {
            return CommandResponse.error("Error",e.getMessage(),e.getMessage().contains("does not") ? "not_found" : "internal_error");
        }
    }

}
