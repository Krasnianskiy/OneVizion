package com.interview.vkras.repository;

import com.interview.vkras.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface BookEntityRepository extends JpaRepository<BookEntity, BigInteger> {
    List<BookEntity> findAllByAuthor(String author);
}
