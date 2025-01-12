package ru.project.booklib.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.booklib.models.Book;
import ru.project.booklib.models.Person;
import ru.project.booklib.repositories.BooksRepository;
import ru.project.booklib.repositories.PeopleRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Page<Person> findAll(int page, int itemsPerPage) {
        return peopleRepository.findAll(PageRequest.of(page - 1, itemsPerPage, Sort.by(Sort.Direction.DESC, "birthyear")));
    }

    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional(readOnly = false)
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional(readOnly = false)
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional(readOnly = false)
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public Boolean hasBooks(int id) {
        Person person = peopleRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found"));
        return !person.getBooks().isEmpty();
    }

    public List<Book> showBooks(int id) {
        Person person = peopleRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found"));
        List<Book> books = person.getBooks();
        LocalDateTime nowDate = LocalDateTime.now();
        for (Book book : books){
            Date bookDate = book.getBooked_at();
            LocalDateTime bookDateTime = bookDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            long daysBetween = ChronoUnit.DAYS.between(bookDateTime, nowDate);
            if (daysBetween > 10){
                book.setExpired(true);
            } else {
                book.setExpired(false);
            }
        }
        return books;
    }
}
