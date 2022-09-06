package com.interview.vkras.controller;

import com.interview.vkras.dto.CharactersInDTO;
import com.interview.vkras.entity.BookEntity;
import com.interview.vkras.repository.BookEntityRepository;
import com.interview.vkras.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private final BookEntityRepository bookEntityRepository;
    private final BookService bookService;

    @GetMapping("/sorted")
    public ResponseEntity<List<BookEntity>> getSortedBooks(){
        // Сортировка с помощью stream api
        List<BookEntity> books = bookService.getAllBooks()
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
    public ResponseEntity<Map<String, List<String>>> getBooksByAuthor(){
        return ResponseEntity.ok(bookService.getBooksGroupByAuthor());
    }

    @GetMapping(path = "/top10")
    public ResponseEntity<List<CharactersInDTO>> getTop10ByCharacter(@RequestParam Character character){
        return ResponseEntity.ok(bookService.getTop10CharacterIn(Character.toLowerCase(character)));
    }

    private ResponseEntity<List<BookEntity>> mapToResponseEntity(List<BookEntity> books){
        if (CollectionUtils.isEmpty(books)){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else return ResponseEntity.ok(books);
    }

}
