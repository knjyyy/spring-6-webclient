package com.spring.spring6webclient.client;

import com.spring.spring6webclient.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient beerClient;

    @Test
    void listBeers() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeers()
                .subscribe(response -> {
                    System.out.println(response);
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void listBeersMap() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerMap()
                .subscribe(response -> {
                    System.out.println(response);
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerJson () {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeersJsonNode().subscribe(jsonNode -> {
            System.out.println(jsonNode.toPrettyString());
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerDTO () {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerDTO().subscribe(beerDTO -> {
            System.out.println(beerDTO.getName());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testGetBeerById() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerDTO()
                .flatMap(dto -> beerClient.getBeerById(dto.getId()))
                .subscribe(beerDTO -> {
                    System.out.println(beerDTO.getName());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }
    @Test
    void testGetBeerByStyle() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.getBeerByStyle("Pale Ale")
                .subscribe(beerDTO -> {
                    System.out.println(beerDTO.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testCreateBeer() {
        AtomicReference<BeerDTO> atomicReference = new AtomicReference<>();
        BeerDTO newBeerDTO = BeerDTO.builder()
                .name("Colt 45")
                .style("IPA")
                .price(new BigDecimal(11))
                .upc("53453")
                .quantityOnHand(111)
                .build();

        beerClient.saveBeer(newBeerDTO)
                .subscribe(beerDTO -> {
                    System.out.println(beerDTO.toString());
                    atomicReference.set(beerDTO);
                });

        await().until(() -> atomicReference.get() != null);
    }

    @Test
    void testUpdateBeer() {
        final String name = "Asahi";
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerDTO()
                .next()
                .doOnNext(beerDTO -> beerDTO.setName(name))
                .flatMap(dto ->beerClient.updateBeer(dto))
                .subscribe(updatedDTO -> {
                    System.out.println(updatedDTO.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testPatchBeer() {
        final String name = "Sapporo";
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerClient.listBeerDTO()
                .next()
                .doOnNext(beerDTO -> beerDTO.setName(name))
                .flatMap(dto -> beerClient.patchBeer(dto))
                .subscribe(patchedDTO -> {
                    System.out.println(patchedDTO.toString());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    void testDeleteBeer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        beerClient.listBeerDTO()
                .next()
                .flatMap(beerDTO -> beerClient.deleteBeer(beerDTO.getId()))
                .doOnSuccess(mt -> atomicBoolean.set(true))
                .subscribe( ) ;
    }
}