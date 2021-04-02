package com.vedam.api.reactiveretail.controller;

import com.vedam.api.reactiveretail.document.Item;
import com.vedam.api.reactiveretail.repository.ItemReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static com.vedam.api.reactiveretail.constants.ItemConstants.ITEM_ENDPOINT_V1;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class ItemControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    private List<Item> data() {
        return Arrays.asList(new Item(null, "Levi's", "501 Original", 35d),
                new Item(null, "Levi's", "514 Straight", 40d),
                new Item(null, "Levi's", "Western Fit", 45d),
                new Item(null, "Levi's", "502 Taper", 55d),
                new Item("XYZ123", "Levi's", "527 Slim BootCut", 50d));
    }

    @BeforeEach
    void setUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted item is " + item))
                .blockLast();
    }

    @Test
    void getAllItems() {
        webTestClient.get().uri(ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(5);
    }

    @Test
    void getAllItems_approach2() {
        webTestClient.get().uri(ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(5)
                .consumeWith(response -> {
                    List<Item> items = response.getResponseBody();
                    assert items != null;
                    items.forEach(item -> assertNotNull(item.getId()));
                });
    }

    @Test
    void getAllItems_approach3() {
        Flux<Item> itemFlux = webTestClient.get().uri(ITEM_ENDPOINT_V1)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Item.class)
                .getResponseBody();
        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }
}