package com.work.thecarpark.controllers;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.repostitory.PersonRepository;
import com.work.thecarpark.service.ServiceCar;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/cars")
public class ControllerCar {
    final ServiceCar serviceCar;
    final PersonRepository personRepository;
    @Autowired
    public ControllerCar(ServiceCar serviceCar, PersonRepository personRepository) {
        this.serviceCar = serviceCar;
        this.personRepository = personRepository;
    }

    @GetMapping()
    public String menuCars(Model model) {
        model.addAttribute("cars", serviceCar.findCars());
        return "cars/index";
    }

    @GetMapping("/{id}")
    public String showCar(@PathVariable("id") int id,
                          Model model,
                          @ModelAttribute("person") Person person) {
        model.addAttribute("car", serviceCar.findCarById(id));
        Person owner = serviceCar.findOwnerCar(id);
        if (owner != null) {
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("people", personRepository.findAll());
        }
        return "cars/show";
    }

    @GetMapping("/{id}/edit")
    public String editCar(Model model, @PathVariable("id") int id) {
        model.addAttribute("car", serviceCar.findCarById(id));
        return "cars/edit";
    }

    @PatchMapping("/{id}")
    public String updateCar(@ModelAttribute("car") Car car, @PathVariable("id") int id) {
        serviceCar.updateCar(id, car);
        return "redirect:/cars";
    }

    @GetMapping("/new")
    public String newCar(@ModelAttribute("car") Car car) {
        return "cars/new";
    }

    @PostMapping()
    public String createCar(@ModelAttribute("car") @Valid Car car, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "cars/new";
        }
        serviceCar.saveCar(car);
        return "redirect:/cars";
    }

    @DeleteMapping("/{id}")
    public String deleteCar(@PathVariable("id") int id) {
        serviceCar.deleteCar(id);
        return "redirect:/cars";
    }

    @PatchMapping("/{id}/choose")
    public String choosePersonForCar(@ModelAttribute("person") Person person,
                                     @PathVariable("id") int id) {
        serviceCar.appointPerson(id, person);
        return "redirect:/cars/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseCar(@PathVariable("id") int id) {
        serviceCar.releaseCar(id);
        return "redirect:/cars/" + id;
    }
}
