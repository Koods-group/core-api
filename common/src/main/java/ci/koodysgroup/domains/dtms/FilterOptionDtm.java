package ci.koodysgroup.domains.dtms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class FilterOptionDtm {

    @JsonProperty("categories")
    private List<CategoryDtm> categories;

    @JsonProperty("media_types")
    private List<MediaTypeDtm> mediaTypes;

    public static FilterOptionDtm fromFilterOptionDtm(List<CategoryDtm> categories, List<MediaTypeDtm> mediaTypes)
    {
        return new FilterOptionDtm(
                categories,
                mediaTypes
        );
    }
}
