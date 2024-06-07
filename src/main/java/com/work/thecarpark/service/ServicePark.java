package com.work.thecarpark.service;

import com.work.thecarpark.entity.cars.Car;
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
    public List<Person> findAllPersons(){
        return personRepository.findAll();
    }
    public Optional<Person> findByFullName(String fullName) {
        return personRepository.findPersonByFullName(fullName);
    }
    public Person findPersonById(int id){
        return personRepository.findById(id).orElseThrow(null);
    }
    @Transactional
    public void savePerson(Person person){
        personRepository.save(person);
    }
    @Transactional
    public void updatePerson(int id, Person person){
        person.setId(id);
        personRepository.save(person);
    }
    @Transactional
    public void deletePerson(int id){
        personRepository.deleteById(id);
    }
    public List<Car> findCarsByPersonId(int id){
        return carsRepository.findCarsByOwner(findPersonById(id));
    }

    //Методы для работы с машинами
    public List<Car> findCars(){
        return carsRepository.findAll();
    }
    public Car findCarById(UUID id){
        return carsRepository.findById(id).orElseThrow(null);
    }
    public Person findOwnerCar(UUID id){
        return findCarById(id).getOwner();
    }
    @Transactional
    public void saveCar(Car car){
        carsRepository.save(car);
    }
    @Transactional
    public void updateCar(UUID id, Car car){
        car.setUniqueNumber(id);
        carsRepository.save(car);
    }
    @Transactional
    public void deleteCar(UUID id){
        carsRepository.deleteById(id);
    }


}
