package com.vedam.api.reactiveretail.repository;

import com.vedam.api.reactiveretail.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DataMongoTest
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    private List<Item> items = Arrays.asList(new Item(null, "Levi's", "501 Original", 35d),
            new Item(null, "Levi's", "514 Straight", 40d),
            new Item(null, "Levi's", "Western Fit", 45d),
            new Item(null, "Levi's", "502 Taper", 55d),
            new Item("XYZ123", "Levi's", "527 Slim BootCut", 50d));

    @BeforeEach
    public void setUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> {
                    System.out.println("inserted item is " + item);
                })
                .blockLast();
        
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();

    }

    @Test
    public void getItemByID(){
        StepVerifier.create(itemReactiveRepository.findById("XYZ123"))
                .expectSubscription()
                .expectNextMatches(item -> item.getStyle().equals("527 Slim BootCut"))
                .verifyComplete();
    }

}