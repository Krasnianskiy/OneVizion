package com.interview.vkras.service;

import com.interview.vkras.dto.CharactersInDTO;
import com.interview.vkras.entity.BookEntity;
import com.interview.vkras.utils.BookScripts;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public BookService(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("book")
                .usingGeneratedKeyColumns("id");
    }

    public List<BookEntity> getAllBooks() {
        return jdbcTemplate.query(BookScripts.FIND_ALL_SQL, new BeanPropertyRowMapper<>(BookEntity.class));
    }

    public BookEntity saveBook(BookEntity bookEntity) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("author", bookEntity.getAuthor());
        parameters.put("title", bookEntity.getTitle());
        parameters.put("description", bookEntity.getDescription());
        bookEntity.setId((Long) simpleJdbcInsert.executeAndReturnKey(parameters));
        return bookEntity;
    }

    public List<CharactersInDTO> getTop10CharacterIn(Character s) {
        // в postgres нет regex_count, пришлось извращаться
        List<CharactersInDTO> result = jdbcTemplate.query(BookScripts.GET_TOP10_SQL, (rs, rowNum) -> {
            CharactersInDTO charactersInDTO = new CharactersInDTO();
            charactersInDTO.setAuthor(rs.getString("author"));
            charactersInDTO.setCount(rs.getString("titles")
                    .chars()
                    .filter(value -> value == s)
                    .count());
            return charactersInDTO;
        }, "%" + s + "%");
        return result.stream()
                .sorted(Comparator.comparing(CharactersInDTO::getCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public Map<String, List<String>> getBooksGroupByAuthor() {
        List<BookEntity> allBooks = jdbcTemplate.query(BookScripts.FIND_TITLES_AMD_AUTHORS_SQL, (rs, rowNum) -> {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setAuthor(rs.getString("author"));
            bookEntity.setTitle(rs.getString("title"));
            return bookEntity;
        });
        return allBooks.stream()
                .collect(Collectors.groupingBy(BookEntity::getAuthor,
                        Collectors.mapping(BookEntity::getTitle, Collectors.toList())));
    }

}
