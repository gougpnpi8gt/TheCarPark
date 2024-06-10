package com.work.thecarpark.util;

import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.service.ServicePerson;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonValidator implements Validator {
    final ServicePerson servicePerson;
    @Autowired
    public PersonValidator(ServicePerson servicePerson) {
        this.servicePerson = servicePerson;
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return  Person.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        if (servicePerson.findByFullName(person.getFullName()).isPresent()){
            errors.rejectValue("fullName", "", "This fullName is already taken");
        }
    }
}