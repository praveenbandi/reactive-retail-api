package com.vedam.api.reactiveretail.handler;

import com.vedam.api.reactiveretail.document.Item;
import com.vedam.api.reactiveretail.repository.ItemReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ItemsHandler {
    private final ItemReactiveRepository itemReactiveRepository;

    public ItemsHandler(ItemReactiveRepository itemReactiveRepository) {
        this.itemReactiveRepository = itemReactiveRepository;
    }

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.findAll(), Item.class);
    }

    public Mono<ServerResponse> getItemByID(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = itemReactiveRepository.findById(id);
        return itemMono.flatMap(item -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(item)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
        Mono<Item> itemMono = serverRequest.bodyToMono(Item.class);
        return itemMono.flatMap(item -> ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.save(item), Item.class));
    }

    public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.deleteById(id), Void.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = serverRequest.bodyToMono(Item.class)
                .flatMap(newItem -> itemReactiveRepository.findById(id)
                        .flatMap(item -> {
                            item.setStyle(newItem.getStyle());
                            item.setBrand(newItem.getBrand());
                            item.setPrice(newItem.getPrice());
                            return itemReactiveRepository.save(item);
                        }));

        return itemMono.flatMap(item -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(item)))
                .switchIfEmpty(ServerResponse.notFound().build());

    }
}
