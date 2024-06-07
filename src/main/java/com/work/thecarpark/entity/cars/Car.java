package com.work.thecarpark.entity.cars;

import com.work.thecarpark.entity.persons.Person;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "unique_number", unique = true)
    UUID uniqueNumber;

    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
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
