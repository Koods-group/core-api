package ci.koodysgroup.features.content.commands;

import ci.koodysgroup.domains.types.LocalizedText;
import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateContentCommand implements Command<String> {
    private UUID id;
    private LocalizedText title;
    private LocalizedText description;
    private boolean mustBeSeen;
    private LocalDate releaseDate;
}
