package ru.project.booklib.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.booklib.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
