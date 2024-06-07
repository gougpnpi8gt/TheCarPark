package com.work.thecarpark.repostitory;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.persons.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findPersonByFullName(String fullName);
}
