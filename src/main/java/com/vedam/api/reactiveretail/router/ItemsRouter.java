package com.vedam.api.reactiveretail.router;

import com.vedam.api.reactiveretail.handler.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.vedam.api.reactiveretail.constants.ItemConstants.ITEM_ENDPOINT_V2;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> itemsRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(GET(ITEM_ENDPOINT_V2).and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::getAllItems)
                .andRoute(GET(ITEM_ENDPOINT_V2 + "/{id}").and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::getItemByID)
                .andRoute(POST(ITEM_ENDPOINT_V2).and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::createItem)
                .andRoute(DELETE(ITEM_ENDPOINT_V2 + "/{id}").and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::deleteItem)
                .andRoute(PUT(ITEM_ENDPOINT_V2 + "/{id}").and(accept(MediaType.APPLICATION_JSON))
                        , itemsHandler::updateItem);
    }
}
