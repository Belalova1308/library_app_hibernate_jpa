package ru.project.booklib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.booklib.models.Book;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

//    @Query(value = "SELECT * FROM Book where name LIKE %:keyword%", nativeQuery = true)
    List<Book> findByNameContainingIgnoreCase( String keyword);
}
