package ru.project.booklib.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.booklib.models.Book;
import ru.project.booklib.models.Person;
import ru.project.booklib.repositories.BooksRepository;
import ru.project.booklib.repositories.PeopleRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;


    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll(){
        return booksRepository.findAll();
    }
    public Page<Book> findAll(int page, int itemsPerPage) {
        return booksRepository.findAll(PageRequest.of(page - 1, itemsPerPage, Sort.by(Sort.Direction.DESC, "year")));
    }
    @Transactional(readOnly = false)
    public void save(Book book){
        booksRepository.save(book);
    }

    public Book findOne(int id){
        Optional<Book> foundBook = booksRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional(readOnly = false)
    public void update(int id, Book book){
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional(readOnly = false)
    public void assignBook(int book_id, int person_id){
        Book book = booksRepository.findById(book_id).orElseThrow(() -> new RuntimeException("Book not found"));
        Person person = peopleRepository.findById(person_id).orElseThrow(() -> new RuntimeException("Person not found"));
        book.setBooked_at(new Date());
        book.setPerson(person);
        booksRepository.save(book);
    }

    public Person findOwner(Book book) {
       return book.getPerson();
    }

    @Transactional(readOnly = false)
    public void freeBook(int id) {
        Book book = booksRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setBooked_at(null);
        book.setPerson(null);
        booksRepository.save(book);
    }
    @Transactional(readOnly = false)
    public void delete (int id) {
        booksRepository.deleteById(id);
    }

    public List<Book> findByKeyword(String keyword){
        return booksRepository.findByNameContainingIgnoreCase(keyword);
    }
}
