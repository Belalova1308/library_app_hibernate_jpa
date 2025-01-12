package ru.project.booklib.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.project.booklib.models.Book;
import ru.project.booklib.models.Person;
import ru.project.booklib.services.BooksService;
import ru.project.booklib.services.PeopleService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }


    @GetMapping()
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        Page<Book> bookPage = booksService.findAll(currentPage, pageSize);
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        List<Person> people = peopleService.findAll();
        Book book = booksService.findOne(id);

        model.addAttribute("book", book);
        model.addAttribute("people", people);
        model.addAttribute("owner", booksService.findOwner(book));
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("id") int id) {

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int bookId,
                         @RequestParam("person_id") int personId) {
        booksService.assignBook(bookId, personId);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{id}/freebook")
    public String freebook(@PathVariable("id") int id) {
        booksService.freeBook(id);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword", required = false) String keyword, Model model){
        List<Book> books = booksService.findByKeyword(keyword);
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        return "books/search";
    }
}
