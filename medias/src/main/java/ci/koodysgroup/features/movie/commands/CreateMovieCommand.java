package ci.koodysgroup.features.movie.commands;

import ci.koodysgroup.interfaces.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Time;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateMovieCommand implements Command<String> {
    private UUID contentId;
    private UUID categoryId;
    private Time duration;
    private String director;
}
