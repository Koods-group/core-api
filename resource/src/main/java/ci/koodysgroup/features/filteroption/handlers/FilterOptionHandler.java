package ci.koodysgroup.features.filteroption.handlers;

import ci.koodysgroup.domains.dtms.CategoryDtm;
import ci.koodysgroup.domains.dtms.CountryDtm;
import ci.koodysgroup.domains.dtms.FilterOptionDtm;
import ci.koodysgroup.domains.dtms.MediaTypeDtm;
import ci.koodysgroup.domains.entities.Category;
import ci.koodysgroup.domains.entities.MediaType;
import ci.koodysgroup.features.filteroption.queries.FilterOptionQuery;
import ci.koodysgroup.interfaces.handlers.QueryHandler;
import ci.koodysgroup.repositories.CategoryRepository;
import ci.koodysgroup.repositories.MediaTypeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class FilterOptionHandler implements QueryHandler<FilterOptionQuery, FilterOptionDtm> {

    private final CategoryRepository categoryRepository;
    private final MediaTypeRepository mediaTypeRepository;

    public FilterOptionHandler(
            CategoryRepository cR,
            MediaTypeRepository mR
    )
    {
        this.categoryRepository = cR;
        this.mediaTypeRepository = mR;
    }

    @Override
    @Transactional(readOnly = true)
    public FilterOptionDtm handler(FilterOptionQuery query) {

        List<Category> categories = this.categoryRepository.findAll();
        List<CategoryDtm> categoryDtms = categories.stream().map(CategoryDtm::fromCategoryDtm)
                                                    .toList();

        List<MediaType> types = this.mediaTypeRepository.findAll();
        List<MediaTypeDtm> mediaTypeDtms = types.stream().map(MediaTypeDtm::fromMediaTypeDtm)
                                            .toList();

        return  FilterOptionDtm.fromFilterOptionDtm(categoryDtms,mediaTypeDtms);
    }
}
