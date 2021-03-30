package com.vedam.api.reactiveretail.repository;

import com.vedam.api.reactiveretail.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
}
