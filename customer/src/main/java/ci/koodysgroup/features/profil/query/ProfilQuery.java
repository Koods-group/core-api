package ci.koodysgroup.features.profil.query;
import ci.koodysgroup.domains.dtms.ProfilDtm;
import ci.koodysgroup.interfaces.query.Query;
import org.springframework.security.core.Authentication;

public class ProfilQuery implements Query<ProfilDtm>{
    private Authentication authentication;
}
