package com.lohika.course.bfffrontend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("api/v1/details")
public class DetailsAgregate {
    public DetailsAgregate(WebClient authorClient, WebClient bookClient) {
        this.authorClient = authorClient;
        this.bookClient = bookClient;
    }

    private final WebClient authorClient;
    private final WebClient bookClient;

    @GetMapping
    public Mono<Map> getBooksAndAuthors() {
/*        int i=0;
        while(i!=15){
            i = ThreadLocalRandom.current().nextInt(0, 500);
        }*/
        Mono<Object> authors = authorClient.get().retrieve().bodyToMono(Object.class);
        Mono<Object> books = bookClient.get().retrieve().bodyToMono(Object.class);
        return authors.zipWith(books).map(t -> {
            Map<String, Object> result = new HashMap<>();
            result.put("authors", t.getT1());
            result.put("books", t.getT2());
            return result;
        });
    }
}
