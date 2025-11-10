package ci.koodysgroup.features.content.handlers;

import ci.koodysgroup.domains.dtms.ContentDtm;
import ci.koodysgroup.domains.entities.Content;
import ci.koodysgroup.domains.entities.MediaType;
import ci.koodysgroup.features.content.commands.UpdateContentCoverCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.ContentRepository;
import ci.koodysgroup.services.FileValidationService;
import ci.koodysgroup.services.S3Service;
import ci.koodysgroup.services.exceptions.FileValidationException;
import ci.koodysgroup.utils.response.CommandResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class UpdateContentCoverHandler implements CommandHandler<UpdateContentCoverCommand, CommandResponse<ContentDtm>> {

    private ContentRepository repository;
    private S3Service s3;
    private FileValidationService fileValidationService;

    @Override
    public CommandResponse<ContentDtm> handler(UpdateContentCoverCommand command) {
        try {

            Content content = repository.findById(command.getId())
                    .orElseThrow(() -> new NoSuchElementException("The referenced content does not seem to exist, please try again ."));

            String cover = null;

            if (command.getCover() != null && !command.getCover().isEmpty()) {
                fileValidationService.validateFile(command.getCover());
                cover = s3.toUpload(command.getCover(), "posters");
            }else {
                throw new FileValidationException("The file is empty or null .");
            }

            String posterUrl = cover != null ? "posters/"+cover : null;

            if(!content.getPosterUrl().isEmpty())
            {
                s3.toDelete(content.getPosterUrl());
            }

            content.setPosterUrl(posterUrl);
            repository.save(content);

            return CommandResponse.success("Cover modified" , ContentDtm.fromContentDtm(content));

        } catch (FileValidationException validationException) {
            return CommandResponse.error("Invalid file" , validationException.getMessage(), "bad_request");
        } catch (Exception e) {
            return CommandResponse.error("Error" , e.getMessage(), e.getMessage().contains("does not") ? "not_found" : "internal_error");
        }
    }
}
