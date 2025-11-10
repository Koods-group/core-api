package ci.koodysgroup.core.controllers.contexts.media.content;

import ci.koodysgroup.domains.types.LocalizedText;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class UpdateContentContext {
    private UUID id;
    private LocalizedText title;
    private LocalizedText description;
    private LocalDate releaseDate;
    private boolean mustBeSeen;
}
