package com.work.thecarpark.controllers;

import com.work.thecarpark.entity.dilers.Dealer;
import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.service.ServiceDealer;
import com.work.thecarpark.service.ServicePerson;
import com.work.thecarpark.util.DealerValidator;
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
@RequestMapping("/dealers")
public class ControllerDealer {
    final ServiceDealer serviceDealer;
    final DealerValidator dealerValidator;
    final ServicePerson servicePerson;
    @Autowired
    public ControllerDealer(ServiceDealer serviceDealer,
                            DealerValidator dealerValidator,
                            ServicePerson servicePerson) {
        this.serviceDealer = serviceDealer;
        this.dealerValidator = dealerValidator;
        this.servicePerson = servicePerson;
    }

    @GetMapping()
    public String menuDealers(Model model) {
        model.addAttribute("dealers", serviceDealer.findAllDealers());
        return "dealers/index";
    }

    @GetMapping("/{id}")
    public String showDealer(@PathVariable("id") int id,
                             Model model,
                             @ModelAttribute("person") Person person
    ) {
        model.addAttribute("dealer", serviceDealer.findDealerById(id));
        List<Person> peopleDealer = serviceDealer.findAllPersonsByDealer(id);
        if (!peopleDealer.isEmpty()) {
            model.addAttribute("persons", peopleDealer);
        } else {
            model.addAttribute("people", servicePerson.findAllPersons());
        }
        return "dealers/show";
//        Dealer dealer = serviceDealer.findDealerByIdWithPersons(id);
//        model.addAttribute("dealer", dealer);
//        if (dealer != null && !dealer.getPersons().isEmpty()) {
//            model.addAttribute("persons", dealer.getPersons());
//        } else {
//            model.addAttribute("people", servicePerson.findAllPersons());
//        }
//        return "dealers/show";
    }

    @GetMapping("/new")
    public String newDealer(@ModelAttribute("dealer") Dealer dealer) {
        return "dealers/new";
    }

    @PostMapping()
    public String createDealer(@ModelAttribute("dealer")
                               @Valid Dealer dealer,
                               BindingResult bindingResult) {
        dealerValidator.validate(dealer, bindingResult);
        if (bindingResult.hasErrors()) {
            return "dealers/new";
        }
        serviceDealer.saveDealer(dealer);
        return "redirect:/dealers";
    }
    @PatchMapping("/{id}/choose")
    public String choosePersonForDealer(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        serviceDealer.appointPerson(id, person);
        return STR."redirect:/dealers/\{id}";
    }

    @GetMapping("/{id}/edit")
    public String editDealer(Model model,
                             @PathVariable("id") int id) {
        model.addAttribute("dealer", serviceDealer.findDealerById(id));
        return "dealers/edit";
    }

    @PatchMapping("/{id}")
    public String updateDealer(@ModelAttribute("dealer") @Valid Dealer dealer,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "dealers/edit";
        serviceDealer.updateDealer(id, dealer);
        return "redirect:/dealers";
    }

    @DeleteMapping("/{id}")
    public String deleteDealer(@PathVariable("id") int id) {
        serviceDealer.deleteDealer(id);
        return "redirect:/dealers";
    }
}
