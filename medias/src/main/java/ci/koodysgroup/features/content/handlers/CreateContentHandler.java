package ci.koodysgroup.features.content.handlers;

import ci.koodysgroup.domains.dtms.ContentDtm;
import ci.koodysgroup.domains.entities.Content;
import ci.koodysgroup.domains.entities.MediaType;
import ci.koodysgroup.features.content.commands.CreateContentCommand;
import ci.koodysgroup.interfaces.handlers.CommandHandler;
import ci.koodysgroup.repositories.ContentRepository;
import ci.koodysgroup.repositories.MediaTypeRepository;
import ci.koodysgroup.services.FileValidationService;
import ci.koodysgroup.services.S3Service;
import ci.koodysgroup.services.exceptions.FileValidationException;
import ci.koodysgroup.utils.response.CommandResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class CreateContentHandler implements CommandHandler<CreateContentCommand , CommandResponse<ContentDtm>> {

    private MediaTypeRepository mediaTypeRepository;
    private ContentRepository repository;
    private S3Service s3;
    private FileValidationService fileValidationService;

    @Override
    public CommandResponse<ContentDtm> handler(CreateContentCommand command) {
        try{

            MediaType mediaType = mediaTypeRepository.findById(command.getMediaType())
                    .orElseThrow(() -> new NoSuchElementException("The selected media type does not appear to exist in the system ."));

            String cover = null;

            if (command.getCover() != null && !command.getCover().isEmpty()) {
                fileValidationService.validateFile(command.getCover());
                cover = s3.toUpload(command.getCover(), "posters");
            }

            else {
                throw new FileValidationException("The file is empty or null .");
            }

            String posterUrl = cover != null ? "posters/"+cover : null;

            Content content = new Content(
                    command.getTitle(),
                    command.isMustBeSeen(),
                    command.getDescription(),
                    command.getReleaseDate(),
                    mediaType,
                    posterUrl
            );
            repository.save(content);

            return CommandResponse.success("Recorded content",ContentDtm.fromContentDtm(content));

        } catch (FileValidationException validationException) {
            return CommandResponse.error("Invalid file" , validationException.getMessage(), "bad_request");
        } catch (Exception e) {
            return CommandResponse.error("Error" , e.getMessage(), e.getMessage().contains("does not") ? "not_found" : "internal_error");
        }
    }
}
