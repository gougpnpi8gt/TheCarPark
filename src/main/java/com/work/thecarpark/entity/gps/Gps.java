package com.work.thecarpark.entity.gps;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "gps")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Gps {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    double latitude;

    double longitude;

    public Gps() {
    }

    public Gps(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
