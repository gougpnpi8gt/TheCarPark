package com.work.thecarpark.service;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.repostitory.CarsRepository;
import com.work.thecarpark.repostitory.PersonRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ServicePerson {
    final PersonRepository personRepository;
    final CarsRepository carsRepository;
    @Autowired
    public ServicePerson(PersonRepository personRepository, CarsRepository carsRepository) {
        this.personRepository = personRepository;
        this.carsRepository = carsRepository;
    }

    public List<Person> findAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> findByFullName(String fullName) {
        return personRepository.findPersonByFullName(fullName);
    }

    public Person findPersonById(int id) {
        return personRepository.findById(id).orElseThrow(null);
    }

    @Transactional
    public void savePerson(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void updatePerson(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    @Transactional
    public void appointCar(int personId, Car car) {
        Person person = findPersonById(personId);
        person.addCar(car);
        car.setOwner(person);
        personRepository.save(person);
    }
    @Transactional
    public void release(int id) {
        Person person = findPersonById(id);
        person.setDealer(null);
    }

    @Transactional
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }
}
