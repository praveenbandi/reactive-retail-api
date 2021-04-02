package com.vedam.api.reactiveretail.controller;

import com.vedam.api.reactiveretail.document.Item;
import com.vedam.api.reactiveretail.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.vedam.api.reactiveretail.constants.ItemConstants.ITEM_ENDPOINT_V1;

@RestController
@Slf4j
public class ItemController {

    private final ItemReactiveRepository itemReactiveRepository;

    public ItemController(ItemReactiveRepository itemReactiveRepository) {
        this.itemReactiveRepository = itemReactiveRepository;
    }

    @GetMapping(ITEM_ENDPOINT_V1)
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }
}
