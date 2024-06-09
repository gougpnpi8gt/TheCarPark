package com.work.thecarpark.util;

import com.work.thecarpark.entity.dilers.Dealer;
import com.work.thecarpark.entity.persons.Person;
import com.work.thecarpark.service.ServicePark;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DealerValidator implements Validator {
    final ServicePark servicePark;
    @Autowired
    public DealerValidator(ServicePark servicePark) {
        this.servicePark = servicePark;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return  Dealer.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Dealer dealer = (Dealer) o;
        if (servicePark.findByFullNameTitle(dealer.getTitle()).isPresent()){
            errors.rejectValue("FullNameRepresentative", "", "This fullName is already taken");
        }
    }
}
