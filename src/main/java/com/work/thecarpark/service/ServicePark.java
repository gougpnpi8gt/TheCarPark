package com.work.thecarpark.service;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.dilers.Dealer;
import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.repostitory.CarsRepository;
import com.work.thecarpark.repostitory.DealersRepository;
import com.work.thecarpark.repostitory.PersonRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ServicePark {
    final PersonRepository personRepository;
    final CarsRepository carsRepository;
    final DealersRepository dealersRepository;

    @Autowired
    public ServicePark(PersonRepository personRepository,
                       CarsRepository carsRepository,
                       DealersRepository dealersRepository) {
        this.personRepository = personRepository;
        this.carsRepository = carsRepository;
        this.dealersRepository = dealersRepository;
    }

    //Методы для работы с владельцами машин
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
        Person person = personRepository.findById(personId).get();
        person.addCar(car);
        car.setOwner(person);
        carsRepository.save(car);
        personRepository.save(person);
    }

    @Transactional
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    public List<Car> findCarsByPersonId(int id) {
        return carsRepository.findCarsByOwner(findPersonById(id));
    }

    //Методы для работы с машинами
    public List<Car> findCars() {
        return carsRepository.findAll();
    }

    public Car findCarById(int id) {
        return carsRepository.findById(id).orElseThrow(null);
    }

    public Person findOwnerCar(int id) {
        return carsRepository.findById(id).map(Car::getOwner).orElse(null);
    }

    @Transactional
    public void releaseCar(int id) {
        Car car = carsRepository.findById(id).get();
        car.setOwner(null);
        Person person = car.getOwner();
        person.deleteCar(car);
        carsRepository.save(car);
        personRepository.save(person);
    }

    @Transactional
    public void appointPerson(int carId, Person owner) {
        Car car = carsRepository.findById(carId).get();
        car.setOwner(owner);
        owner.addCar(car);
        personRepository.save(owner);
        carsRepository.save(car);
    }

    @Transactional
    public void saveCar(Car car) {
        carsRepository.save(car);
    }

    @Transactional
    public void updateCar(int id, Car car) {
        car.setId(id);
        carsRepository.save(car);
    }

    @Transactional
    public void deleteCar(int id) {
        carsRepository.deleteById(id);
    }

    //Методы для работы с дилерами
    public List<Dealer> findAllDealers() {
        return dealersRepository.findAll();
    }

    public Optional<Dealer> findByFullNameTitle(String title) {
        return dealersRepository.findByTitle(title);
    }

    public List<Person> findAllPersonsByDealer(int id) {
        return dealersRepository.findDealerById(id).getPersons();
    }

    public Dealer findDealerById(int id) {
        return dealersRepository.findById(id).orElseThrow(null);
    }

    @Transactional
    public void saveDealer(Dealer dealer) {
        dealersRepository.save(dealer);
    }

    @Transactional
    public void updateDealer(int id, Dealer dealer) {
        dealer.setId(id);
        dealersRepository.save(dealer);
    }

    @Transactional
    public void deleteDealer(int id) {
        dealersRepository.deleteById(id);
    }

}