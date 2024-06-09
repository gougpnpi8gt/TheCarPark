package com.work.thecarpark.controllers;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.dilers.Dealer;
import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.service.ServicePark;
import com.work.thecarpark.util.DealerValidator;
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
import java.util.UUID;

@Controller
@RequestMapping("/park")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ControllerPark {
    final DealerValidator dealerValidator;
    final ServicePark servicePark;
    final PersonValidator personValidator;

    @Autowired
    public ControllerPark(DealerValidator dealerValidator,
                          ServicePark servicePark,
                          PersonValidator personValidator) {
        this.dealerValidator = dealerValidator;
        this.servicePark = servicePark;
        this.personValidator = personValidator;
    }

    /*
    Меню
     */
    @GetMapping()
    public String index() {
        return "main/index";
    }

    /*
     Методы для работы с владельцами машин
     */
    @GetMapping("/persons")
    public String menuPersons(Model model) {
        model.addAttribute("people", servicePark.findAllPersons());
        return "people/index";
    }

    @GetMapping("/persons/{id}")
    public String showPerson(Model model,
                             @PathVariable("id") int id,
                             @ModelAttribute("car") Car car) {
        model.addAttribute("person", servicePark.findPersonById(id));
        List<Car> carsOwner = servicePark.findCarsByPersonId(id);
        if (carsOwner.isEmpty()) {
            model.addAttribute("cars", servicePark.findCars());
        } else {
            model.addAttribute("carsOwner", carsOwner);
        }
        return "people/show";
    }

    @PatchMapping("/persons/{id}/choose")
    public String chooseCarForPerson(@ModelAttribute("car") Car car,
                                     @PathVariable("id") int id) {
        servicePark.appointCar(id, car);
        return "redirect:/park/persons/" + id;
    }

    @GetMapping("/persons/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping("/persons")
    public String create(@ModelAttribute("person")
                         @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        servicePark.savePerson(person);
        return "redirect:/park/persons";
    }

    @GetMapping("/persons/{id}/edit")
    public String editPerson(Model model,
                             @PathVariable("id") int id) {
        model.addAttribute("person", servicePark.findPersonById(id));
        return "people/edit";
    }

    @PatchMapping("/persons/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";
        servicePark.updatePerson(id, person);
        return "redirect:/park/persons";
    }

    @DeleteMapping("/persons/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        servicePark.deletePerson(id);
        return "redirect:/park/persons";
    }

    /*
    методы контролера для работы с машинами
     */

    @GetMapping("/cars")
    public String menuCars(Model model) {
        model.addAttribute("cars", servicePark.findCars());
        return "cars/index";
    }

    @GetMapping("/cars/{id}")
    public String showCar(@PathVariable("id") int id,
                          Model model,
                          @ModelAttribute("person") Person person) {
        model.addAttribute("car", servicePark.findCarById(id));
        Person owner = servicePark.findOwnerCar(id);
        if (owner != null) {
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("people", servicePark.findAllPersons());
        }
        return "cars/show";
    }

    @GetMapping("/cars/{id}/edit")
    public String editCar(Model model, @PathVariable("id") int id) {
        model.addAttribute("car", servicePark.findCarById(id));
        return "cars/edit";
    }

    @PatchMapping("/cars/{id}")
    public String updateCar(@ModelAttribute("car") Car car, @PathVariable("id") int id) {
        servicePark.updateCar(id, car);
        return "redirect:/park/cars";
    }

    @GetMapping("/cars/new")
    public String newCar(@ModelAttribute("car") Car car) {
        return "cars/new";
    }

    @PostMapping("/cars")
    public String createCar(@ModelAttribute("car") @Valid Car car, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "cars/new";
        }
        servicePark.saveCar(car);
        return "redirect:/park/cars";
    }

    @DeleteMapping("/cars/{id}")
    public String deleteCar(@PathVariable("id") int id) {
        servicePark.deleteCar(id);
        return "redirect:/park/cars";
    }

    @PatchMapping("/cars/{id}/choose")
    public String choosePersonForCar(@ModelAttribute("person") Person person,
                                     @PathVariable("id") int id) {
        servicePark.appointPerson(id, person);
        return "redirect:/park/cars/" + id;
    }

    @PatchMapping("/cars/{id}/release")
    public String releaseCar(@PathVariable("id") int id) {
        servicePark.releaseCar(id);
        return "redirect:/park/cars/" + id;
    }

    /*
     Методы для работы с дилерами
     */
    @GetMapping("/dealers")
    public String menuDealers(Model model) {
        model.addAttribute("dealers", servicePark.findAllDealers());
        return "dealers/index";
    }

    @GetMapping("/dealers/{id}")
    public String showDealer(@PathVariable("id") int id,
                             Model model
                             //@ModelAttribute("person") Person person
    ) {
        model.addAttribute("dealer", servicePark.findDealerById(id));
        List<Person> people = servicePark.findAllPersonsByDealer(id);
        if (people != null) {
            model.addAttribute("people", people);
        }
        return "dealers/show";
    }

    @GetMapping("/dealers/new")
    public String newDealer(@ModelAttribute("dealer") Dealer dealer) {
        return "dealers/new";
    }

    @PostMapping("/dealers")
    public String createDealer(@ModelAttribute("dealer")
                               @Valid Dealer dealer,
                               BindingResult bindingResult) {
        dealerValidator.validate(dealer, bindingResult);
        if (bindingResult.hasErrors()) {
            return "dealers/new";
        }
        servicePark.saveDealer(dealer);
        return "redirect:/park/dealers";
    }

    @GetMapping("/dealers/{id}/edit")
    public String editDealer(Model model,
                             @PathVariable("id") int id) {
        model.addAttribute("dealer", servicePark.findDealerById(id));
        return "dealers/edit";
    }

    @PatchMapping("/dealers/{id}")
    public String updateDealer(@ModelAttribute("dealer") @Valid Dealer dealer,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "dealers/edit";
        servicePark.updateDealer(id, dealer);
        return "redirect:/park/dealers";
    }

    @DeleteMapping("/dealers/{id}")
    public String deleteDealer(@PathVariable("id") int id) {
        servicePark.deleteDealer(id);
        return "redirect:/park/dealers";
    }
}