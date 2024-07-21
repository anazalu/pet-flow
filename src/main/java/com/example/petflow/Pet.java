package com.example.petflow;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pet {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer dob;

    enum Species {
        CAT,
        DOG,
        BIRD,
        TURTLE,
        FISH,
        OTHER
    }

    @Enumerated(EnumType.STRING)
    private Species species;
}
