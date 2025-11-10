package ci.koodysgroup.features.content.commands;

import ci.koodysgroup.domains.types.LocalizedText;
import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateContentCommand implements Command<String> {
    private LocalizedText title;
    private LocalizedText description;
    private LocalDate releaseDate;
    private UUID mediaType;
    private boolean mustBeSeen;
    private MultipartFile cover;
}
