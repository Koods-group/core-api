package ci.koodysgroup.domains.dtms;


import ci.koodysgroup.domains.entities.Access;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
public class AccessDtm {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("civility")
    private String civility;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("token")
    private String token;

    @JsonProperty("authorities")
    private Collection<String> authorities;

    public static AccessDtm fromAccessDtm(Access access , String token){
        return new AccessDtm(
                access.getId(),
                access.getUser().getCivility(),
                access.getUser().getName(),
                access.getUser().getSurname(),
                token,
                access.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
    }

}
