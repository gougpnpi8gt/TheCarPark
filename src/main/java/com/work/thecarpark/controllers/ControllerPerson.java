package com.work.thecarpark.controllers;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.dilers.Dealer;
import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.service.ServiceCar;
import com.work.thecarpark.service.ServiceDealer;
import com.work.thecarpark.service.ServicePerson;
import com.work.thecarpark.util.PersonValidator;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/persons")
public class ControllerPerson {
    final ServicePerson servicePerson;
    final ServiceCar serviceCar;
    final PersonValidator personValidator;
    final ServiceDealer serviceDealer;
    @Autowired
    public ControllerPerson(ServicePerson servicePerson,
                            ServiceCar serviceCar,
                            PersonValidator personValidator,
                            ServiceDealer serviceDealer) {
        this.servicePerson = servicePerson;
        this.serviceCar = serviceCar;
        this.personValidator = personValidator;
        this.serviceDealer = serviceDealer;
    }

    @GetMapping()
    public String menuPersons(Model model) {
        model.addAttribute("people", servicePerson.findAllPersons());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String showPerson(Model model,
                             @PathVariable("id") int id,
                             @ModelAttribute("car") Car car,
                             @ModelAttribute("dealer") Dealer dealer) {
        Person owner = servicePerson.findPersonById(id);
        model.addAttribute("dealers", serviceDealer.findAllDealers());
        model.addAttribute("person", owner);
        List<Car> carsOwner = owner.getCars();
        if (carsOwner.isEmpty()) {
            model.addAttribute("cars", serviceCar.findCars());
        } else {
            model.addAttribute("carsOwner", carsOwner);
        }
        return "people/show";
    }

    @PatchMapping("/{id}/chooseCar")
    public String chooseCarForPerson(@ModelAttribute("car") Car car,
                                     @PathVariable("id") int id) {
        servicePerson.appointCar(id, car);
        return STR."redirect:/persons/\{id}";
    }

    @PatchMapping("/{id}/chooseDealer")
    public String chooseDealerForPerson(@ModelAttribute("dealer") Dealer dealer,
                                        @PathVariable("id") int id) {
        servicePerson.appointDealer(id, dealer);
        return STR."redirect:/persons/\{id}";
    }
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person")
                         @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        servicePerson.savePerson(person);
        return "redirect:/persons";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(Model model,
                             @PathVariable("id") int id) {
        model.addAttribute("person", servicePerson.findPersonById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";
        servicePerson.updatePerson(id, person);
        return "redirect:/persons";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        servicePerson.deletePerson(id);
        return "redirect:/persons";
    }
    @PatchMapping("/{id}/releaseDealer")
    public String releaseDealer(@PathVariable("id") int id){
        servicePerson.release(id);
        return STR."redirect:/persons/\{id}";
    }
}
