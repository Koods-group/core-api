package ci.koodysgroup.domains.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocalizedText {

    @JsonProperty("fr")
    private String fr;

    @JsonProperty("en")
    private String en;

    public String get(String lang) {
        if ("fr".equalsIgnoreCase(lang)) return fr;
        if ("en".equalsIgnoreCase(lang)) return en;
        return fr;
    }

}
