package guru.springframework.reactivemongo.services;

import guru.springframework.reactivemongo.mappers.BeerMapper;
import guru.springframework.reactivemongo.model.BeerDTO;
import guru.springframework.reactivemongo.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;

    @Override
    public Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO) {
        return beerDTO.map(this.beerMapper::beerDTOToBeer)
                .flatMap(this.beerRepository::save)
                .map(this.beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> getById(String beerId) {
        return null;
    }
}
