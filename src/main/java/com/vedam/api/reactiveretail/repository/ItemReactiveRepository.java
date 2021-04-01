package com.vedam.api.reactiveretail.repository;

import com.vedam.api.reactiveretail.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

    Flux<Item> findByBrand(String brandName);
}
