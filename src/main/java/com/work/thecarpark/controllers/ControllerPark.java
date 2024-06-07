package com.work.thecarpark.controllers;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.service.ServicePark;
import com.work.thecarpark.util.PersonValidator;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/park")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ControllerPark {
    final ServicePark servicePark;
    final PersonValidator personValidator;

    @Autowired
    public ControllerPark(ServicePark servicePark,
                          PersonValidator personValidator) {
        this.servicePark = servicePark;
        this.personValidator = personValidator;
    }
    // Меню
    @GetMapping()
    public String index() {
        return "main/index";
    }

    // Методы для работы с владельцами машин
    @GetMapping("/persons")
    public String menuPersons(Model model) {
        model.addAttribute("people", servicePark.findAllPersons());
        return "people/index";
    }

    @GetMapping("/persons/{id}")
    public String showPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", servicePark.findPersonById(id));
        model.addAttribute("cars", servicePark.findCarsByPersonId(id));
        return "people/show";
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

    @GetMapping("persons/{id}/edit")
    public String editPerson(Model model,
                       @PathVariable("id") int id) {
        model.addAttribute("person", servicePark.findPersonById(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "people/edit";
        servicePark.updatePerson(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        servicePark.deletePerson(id);
        return "redirect:/persons";
    }
    // методы контролера для работы с машинами

    @GetMapping("/cars")
    public String menuCars(Model model){
        model.addAttribute("cars", servicePark.findCars());
        return "cars/index";
    }
    @GetMapping("/cars/{id}")
    public String showCar(@PathVariable("id") UUID id,
                       Model model,
                       @ModelAttribute("person") Car car){
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
    public String editCar(Model model, @PathVariable("id") UUID id){
        model.addAttribute("car", servicePark.findCarById(id));
        return "cars/edit";
    }
    @PatchMapping("/cars/{id}")
    public String updateCar(@ModelAttribute("car") Car car, @PathVariable("id") UUID id){
        servicePark.updateCar(id, car);
        return "redirect:/cars";
    }
    @GetMapping("/cars/new")
    public String newCar(@ModelAttribute("car") Car car){
        return "cars/new";
    }

    @PostMapping("/cars")
    public String createCar(@ModelAttribute("car") @Valid Car car){
        servicePark.saveCar(car);
        return "redirect:/cars";
    }
    @DeleteMapping("/cars/{id}")
    public String deleteCar(@PathVariable("id") UUID id){
        servicePark.deleteCar(id);
        return "redirect:/cars";
    }
//    @PatchMapping("/cars/{id}/choose")
//    public String choosePersonForCar(@ModelAttribute("person") Person person,
//                                     @PathVariable("id") int id){
//        servicePark.appointPerson(id, person);
//        return "redirect:/cars/" + id;
//    }

//    @PatchMapping("/{id}/release")
//    public String releaseCar(@PathVariable("id") int id){
//        servicePark.release(id);
//        return "redirect:/cars/" + id;
//    }
    @GetMapping("/cars/search")
    public String search(){
        return "cars/search";
    }
//    @PostMapping("/cars/search")
//    public String searchBook(@RequestParam("query") String query, Model model){
//        model.addAttribute("cars", servicePark.searchCarBeginWithName(query));
//        return "cars/search";
//    }


    // Методы для работы с дилерами
}
