package com.interview.vkras.controller;

import com.interview.vkras.entity.BookEntity;
import com.interview.vkras.repository.BookEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private final BookEntityRepository bookEntityRepository;

    @GetMapping("/sorted")
    public ResponseEntity<List<BookEntity>> getSortedBooks(){
        // Сортировка с помощью stream api
        List<BookEntity> books = bookEntityRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(BookEntity::getTitle).reversed())
                .collect(Collectors.toList());
        return mapToResponseEntity(books);
    }

    @PostMapping
    public ResponseEntity<BookEntity> addBook(@RequestBody BookEntity bookEntity){
        // Добавлен hibernate sequence для автогенерации id, в связи с этим сохранение не упадёт, если id = null
        return ResponseEntity.ok(bookEntityRepository.save(bookEntity));
    }

    @GetMapping
    public ResponseEntity<List<BookEntity>> getBooksByAuthor(@RequestParam String author){
        return mapToResponseEntity(bookEntityRepository.findAllByAuthor(author));
    }

    private ResponseEntity<List<BookEntity>> mapToResponseEntity(List<BookEntity> books){
        if (CollectionUtils.isEmpty(books)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else return ResponseEntity.ok(books);
    }

}
