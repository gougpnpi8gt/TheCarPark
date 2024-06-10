package com.work.thecarpark.entity.dilers;

import com.work.thecarpark.entity.persons.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "dealers")
public class Dealer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Cascade(value = org.hibernate.annotations.CascadeType.REFRESH)
    Integer id;

    @Column(name = "title")
    @Size(min = 1, max = 100, message = "Название должно быть от 1 до 100 символов длиной")
    String title;

    @Column(name = "full_name_representative")
    @Size(min = 2, max = 100, message = "ФИО представителя должно быть от 2 до 100 символов длиной")
    String fullNameRepresentative;

    @Email(message = "Сообщение должно иметь формат адреса электронной почты")
    @Column(name = "email")
    String email;

    @OneToMany(mappedBy = "dealer", cascade = CascadeType.ALL)
    List<Person> persons;

    public void addPerson(Person person){
        if (persons == null){
            persons = new ArrayList<>();
        }
        persons.add(person);
        //person.setDealer(this);
    }
}
