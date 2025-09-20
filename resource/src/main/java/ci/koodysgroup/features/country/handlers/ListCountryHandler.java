package ci.koodysgroup.features.country.handlers;

import ci.koodysgroup.domains.dtms.CountryDtm;
import ci.koodysgroup.features.country.queries.ListCountryQuery;
import ci.koodysgroup.interfaces.handlers.QueryHandler;
import ci.koodysgroup.repositories.CountryRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ListCountryHandler implements QueryHandler<ListCountryQuery , List<CountryDtm>> {

    private final CountryRepository repository ;


    public ListCountryHandler(CountryRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public List<CountryDtm> handler(ListCountryQuery query) {
        return this.repository.findAll().stream()
                .map(CountryDtm::fromCountryDtm)
                .toList();
    }
}
