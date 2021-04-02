package com.vedam.api.reactiveretail.intialize;

import com.vedam.api.reactiveretail.document.Item;
import com.vedam.api.reactiveretail.repository.ItemReactiveRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class InitializeItemData implements CommandLineRunner {

    private final ItemReactiveRepository itemReactiveRepository;

    public InitializeItemData(ItemReactiveRepository itemReactiveRepository) {
        this.itemReactiveRepository = itemReactiveRepository;
    }

    @Override
    public void run(String... args) {
        initialDataSetUp();
    }

    private void initialDataSetUp() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> System.out.println("Item inserted from CommandLineRunner " + item));
    }

    private List<Item> data() {
        return Arrays.asList(new Item(null, "Levi's", "501 Original", 35d),
                new Item(null, "Levi's", "514 Straight", 40d),
                new Item(null, "Levi's", "Western Fit", 45d),
                new Item(null, "Levi's", "502 Taper", 55d),
                new Item("XYZ123", "Levi's", "527 Slim BootCut", 50d));
    }
}
