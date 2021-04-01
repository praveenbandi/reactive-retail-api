package com.vedam.api.reactiveretail.repository;

import com.vedam.api.reactiveretail.document.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    private final List<Item> items = Arrays.asList(new Item(null, "Levi's", "501 Original", 35d),
            new Item(null, "Levi's", "514 Straight", 40d),
            new Item(null, "Levi's", "Western Fit", 45d),
            new Item(null, "Levi's", "502 Taper", 55d),
            new Item("XYZ123", "Levi's", "527 Slim BootCut", 50d));

    @BeforeEach
    public void setUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("inserted item is " + item))
                .blockLast();
        
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();

    }

    @Test
    public void getItemByID(){
        StepVerifier.create(itemReactiveRepository.findById("XYZ123"))
                .expectSubscription()
                .expectNextMatches(item -> item.getStyle().equals("527 Slim BootCut"))
                .verifyComplete();
    }


    @Test
    void findByBrand() {
        StepVerifier.create(itemReactiveRepository.findByBrand("Levi's"))
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    void saveItem() {
        Item item = new Item(null, "Diesel", "Slim Jeans", 275d);
        Mono<Item> savedItem = itemReactiveRepository.save(item);
        StepVerifier.create(savedItem.log("Saved Item: "))
                .expectSubscription()
                .expectNextMatches(item1 -> item1.getId() !=null && item1.getBrand().equals("Diesel"))
                .verifyComplete();
    }

    @Test
    void updateItem() {
        Mono<Item> updatedItem = itemReactiveRepository.findById("XYZ123")
                .map(item1 -> {
                    item1.setPrice(100d);
                    return item1;
                })
                .flatMap(itemReactiveRepository::save);

        StepVerifier.create(updatedItem.log("Updated Item: "))
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice().equals(100d))
                .verifyComplete();
    }

    @Test
    void deleteItem() {
        Flux<Void> deletedItem = itemReactiveRepository.findByBrand("Diesel")
                .map(Item::getId)
                .flatMap(id -> itemReactiveRepository.deleteById(id));

        StepVerifier.create(deletedItem.log("deleted item:"))
                .expectSubscription()
                .verifyComplete();
    }
}