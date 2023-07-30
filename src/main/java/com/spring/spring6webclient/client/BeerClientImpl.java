package com.spring.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.spring.spring6webclient.model.BeerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class BeerClientImpl implements BeerClient {
    private static final String BEER_API = "/api/v3/beer";
    private static final String BEER_API_ID = BEER_API + "/{id}";

    WebClient webClient;

    public BeerClientImpl(WebClient.Builder builder) {
        this.webClient = builder.build();
    }
    @Override
    public Flux<String> listBeers() {
        return webClient.get().uri(BEER_API)
                .retrieve().bodyToFlux(String.class);
    }

    @Override
    public Flux<Map> listBeerMap() {
        return webClient.get().uri(BEER_API)
                .retrieve().bodyToFlux(Map.class);
    }

    @Override
    public Flux<JsonNode> listBeersJsonNode() {
        return webClient.get().uri(BEER_API)
                .retrieve().bodyToFlux(JsonNode.class);
    }

    @Override
    public Flux<BeerDTO> listBeerDTO() {
        return webClient.get().uri(BEER_API)
                .retrieve().bodyToFlux(BeerDTO.class);
    }

    @Override
    public Mono<BeerDTO> getBeerById(String id) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path(BEER_API_ID).build(id))
                .retrieve()
                .bodyToMono(BeerDTO.class);
    }

    @Override
    public Flux<BeerDTO> getBeerByStyle(String style) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path(BEER_API)
                .queryParam("style", style).build())
                .retrieve()
                .bodyToFlux(BeerDTO.class );
    }

    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDTO) {
        return webClient.post().uri(BEER_API)
                .body(Mono.just(beerDTO), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> Mono.just(voidResponseEntity.getHeaders().get("Location").get(0)))
                .map(path -> path.split("/")[path.split("/").length - 1])
                .flatMap(this::getBeerById);
    }

    @Override
    public Mono<BeerDTO> updateBeer(BeerDTO beerDTO) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(BEER_API_ID).build(beerDTO.getId()))
                .body(Mono.just(beerDTO), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(beerDTO.getId()));
    }

    @Override
    public Mono<BeerDTO> patchBeer(BeerDTO beerDTO) {
        return webClient.patch()
                .uri(uriBuilder -> uriBuilder.path(BEER_API_ID).build(beerDTO.getId()))
                .body(Mono.just(beerDTO), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(beerDTO.getId()));
    }

    @Override
    public Mono<Void> deleteBeer(String id) {
        return webClient.delete().uri(uriBuilder -> uriBuilder.path(BEER_API_ID).build(id))
                .retrieve()
                .toBodilessEntity()
                .then();
    }

}
