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

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional(readOnly = true)
public class ServiceCar {
    final CarsRepository carsRepository;
    final PersonRepository personRepository;
    @Autowired
    public ServiceCar(CarsRepository carsRepository, PersonRepository personRepository) {
        this.carsRepository = carsRepository;
        this.personRepository = personRepository;
    }

    public List<Car> findCarsByPersonId(int id) {
        return carsRepository.findCarsByOwner(personRepository.findPersonById(id));
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
        Car car = findCarById(id);
        car.setOwner(null);
    }

    @Transactional
    public void appointPerson(int carId, Person owner) {
        Car car = findCarById(carId);
        car.setOwner(owner);
        owner.addCar(car);
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
}
