package ci.koodysgroup.domains.dtms;

import ci.koodysgroup.domains.entities.Category;
import ci.koodysgroup.domains.types.LocalizedText;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class CategoryDtm {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("libelle")
    private LocalizedText libelle;

    @JsonProperty("description")
    private LocalizedText description;

    public static CategoryDtm fromCategoryDtm(Category category)
    {
        return new CategoryDtm(
                category.getId(),
                category.getLibelle(),
                category.getDescription()
        );
    }
}
