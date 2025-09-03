package ci.koodysgroup.core.controllers;

import ci.koodysgroup.domains.dtms.CountryDtm;
import ci.koodysgroup.features.country.queries.ListCountryQuery;
import ci.koodysgroup.interfaces.bus.QueryBus;
import ci.koodysgroup.utils.response.ApiResponse;
import ci.koodysgroup.utils.response.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/resource")
public class ResourceController {

    private final QueryBus bus ;

    public ResourceController(QueryBus bus)
    {
        this.bus = bus;
    }

    @GetMapping("countries")
    public ResponseEntity<ApiResponse<List<CountryDtm>>> countries()
    {
        ListCountryQuery query = new ListCountryQuery();
        List<CountryDtm> countries = this.bus.send(query);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponseUtil.success(countries));
    }
}
