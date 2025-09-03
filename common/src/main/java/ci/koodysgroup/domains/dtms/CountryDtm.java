package ci.koodysgroup.domains.dtms;

import ci.koodysgroup.domains.entities.Country;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CountryDtm {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("libelle")
    private String libelle;

    @JsonProperty("alias")
    private String alias;

    @JsonProperty("flag")
    private String flag;

    @JsonProperty("number_length")
    private int numberLength;

    @JsonProperty("country_code")
    private String countryCode;


    public static CountryDtm fromCountryDtm(Country country) {
        return new CountryDtm(
                country.getId(),
                country.getLibelle(),
                country.getAlias(),
                country.getFlag(),
                country.getNumber_length(),
                country.getCountry_code()
        );
    }
}