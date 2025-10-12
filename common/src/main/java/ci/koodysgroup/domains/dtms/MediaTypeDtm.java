package ci.koodysgroup.domains.dtms;

import ci.koodysgroup.domains.entities.MediaType;
import ci.koodysgroup.domains.types.LocalizedText;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class MediaTypeDtm {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("libelle")
    private LocalizedText libelle;

    public static MediaTypeDtm fromMediaTypeDtm(MediaType type)
    {
        return new MediaTypeDtm(type.getId(),type.getLibelle());
    }

}
