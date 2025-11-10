package ci.koodysgroup.features.content.commands;

import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateContentCoverCommand implements Command<String> {
    private UUID id;
    private MultipartFile cover;
}
