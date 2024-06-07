package com.work.thecarpark.entity.persons;

import com.work.thecarpark.entity.cars.Car;
import com.work.thecarpark.entity.dilers.Dealer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "persons")
public class Person {

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Integer id;

        @Column(name = "full_name")
        @Size(min = 2, max = 100, message = "ФИО должно быть от 2 до 100 символов длиной")
        String fullName;

        @Column(name = "email")
        @Email(message = "Сообщение должно иметь формат адреса электронной почты")
        String email;

        @Column(name = "phone_number")
        @Pattern(regexp = "^\\+7\\(\\d{3}\\)-\\d{3}-\\d{2}-\\d{2}$", message = "Не верный формат номера телефона")
        String phone;

        @OneToMany(mappedBy = "owner")
        List<Car> cars;

        @ManyToOne
        @JoinColumn(name = "dealer_id",
                referencedColumnName = "id")
        Dealer dealer;

        private void addCar(Car car){
            if (car == null){
                cars = new ArrayList<>();
            }
            cars.add(car);
        }
}
