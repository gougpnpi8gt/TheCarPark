package com.work.thecarpark.entity.cars;

import com.work.thecarpark.entity.persons.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Cascade(value = org.hibernate.annotations.CascadeType.REFRESH)
    Integer id;

    @Column(name = "unique_number", unique = true)
    @NotEmpty(message = "Уникальный номер должен быть назначен")
    @Size(min = 2, max = 100, message = "Поле должно быть от 2 до 20 символов длиной")
    String uniqueNumber;

    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @PastOrPresent(message = "Сообщение должно содержать дату сборки в прошлом или настоящем")
    LocalDate dateCreation;

    @ManyToOne
    @JoinColumn(name = "person_id",
            referencedColumnName = "id")
    Person owner;
}
