package com.work.thecarpark.entity.cars;

import com.work.thecarpark.entity.persons.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    Integer id;

    @Column(name = "unique_number", unique = true)
    UUID uniqueNumber;

    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @PastOrPresent(message = "Сообщение должно содержать дату сборки в прошлом или настоящем")
    LocalDate dateCreation;

    @ManyToOne
    @JoinColumn(name = "person_id",
            referencedColumnName = "id")
    Person owner;
    @PrePersist
    private void generateUniqueNumber() {
        if (uniqueNumber == null) {
            uniqueNumber = UUID.fromString(String.valueOf(UUID.randomUUID()));
        }
    }
}
