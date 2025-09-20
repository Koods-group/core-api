package ci.koodysgroup.features.profil.handler;

import ci.koodysgroup.domains.dtms.ProfilDtm;
import ci.koodysgroup.features.profil.query.ProfilQuery;
import ci.koodysgroup.interfaces.handlers.QueryHandler;
import ci.koodysgroup.repositories.AccessRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProfilHandler implements QueryHandler<ProfilQuery , ProfilDtm> {

    private final AccessRepository repository;

    @Override
    public ProfilDtm handler(ProfilQuery query) {
        return null;
    }
}
