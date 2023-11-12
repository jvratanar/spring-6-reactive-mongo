package guru.springframework.reactivemongo.mappers;

import guru.springframework.reactivemongo.domain.Beer;
import guru.springframework.reactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {
    Beer beerDTOToBeer(BeerDTO dto);
    BeerDTO beerToBeerDTO(Beer beer);
}
