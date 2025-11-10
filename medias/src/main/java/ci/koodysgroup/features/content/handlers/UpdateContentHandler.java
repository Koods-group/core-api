package ci.koodysgroup.features.content.handlers;

import ci.koodysgroup.domains.dtms.ContentDtm;
import ci.koodysgroup.domains.entities.Content;
import ci.koodysgroup.features.content.commands.UpdateContentCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.ContentRepository;
import ci.koodysgroup.utils.response.CommandResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class UpdateContentHandler implements CommandHandler<UpdateContentCommand , CommandResponse<ContentDtm>> {

    private ContentRepository repository;

    @Override
    public CommandResponse<ContentDtm> handler(UpdateContentCommand command) {

        try {

            Content content = repository.findById(command.getId())
                    .orElseThrow(() -> new NoSuchElementException("The referenced content does not seem to exist, please try again ."));

            content.setDescription(command.getDescription());
            content.setTitle(command.getTitle());
            content.setMustBeSeen(command.isMustBeSeen());
            content.setReleaseDate(command.getReleaseDate());

            repository.save(content);

            return CommandResponse.success("Information modified" , ContentDtm.fromContentDtm(content));

        } catch (Exception e) {
            return CommandResponse.error("Error" , e.getMessage(), e.getMessage().contains("does not") ? "not_found" : "internal_error");
        }

    }
}
