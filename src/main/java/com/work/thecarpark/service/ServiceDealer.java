package com.work.thecarpark.service;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.dilers.Dealer;
import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.repostitory.DealersRepository;
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
public class ServiceDealer {
    final DealersRepository dealersRepository;
    final PersonRepository personRepository;
    @Autowired
    public ServiceDealer(DealersRepository dealersRepository, PersonRepository personRepository) {
        this.dealersRepository = dealersRepository;
        this.personRepository = personRepository;
    }

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
    public void appointPerson(int dealerId, Person person) {
        Dealer dealer = findDealerById(dealerId);
        dealer.addPerson(person);
        person.setDealer(dealer);
    }
    @Transactional
    public void deleteDealer(int id) {
        dealersRepository.deleteById(id);
    }
}
