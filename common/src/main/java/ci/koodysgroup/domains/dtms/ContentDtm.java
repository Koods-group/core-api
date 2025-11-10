package ci.koodysgroup.domains.dtms;

import ci.koodysgroup.domains.entities.Content;
import ci.koodysgroup.domains.types.LocalizedText;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class ContentDtm {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("title")
    private LocalizedText title;

    @JsonProperty("description")
    private LocalizedText description;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    @JsonProperty("must_be_seen")
    private boolean mustBeSeen;


    @JsonProperty("posterUrl")
    private String posterUrl;




    public static ContentDtm fromContentDtm(Content content)
    {
        return new ContentDtm(
                content.getId(),
                content.getTitle(),
                content.getDescription(),
                content.getReleaseDate(),
                content.isMustBeSeen(),
                content.getPosterUrl()
        );
    }


}
