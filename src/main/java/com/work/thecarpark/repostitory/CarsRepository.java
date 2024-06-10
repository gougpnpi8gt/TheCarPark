package com.work.thecarpark.repostitory;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.persons.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CarsRepository extends JpaRepository<Car, Integer> {
    List<Car> findCarsByOwner(Person person);
    Car findCarById(int id);
}
