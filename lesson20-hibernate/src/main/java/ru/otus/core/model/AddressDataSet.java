package ru.otus.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "addresses")
@NoArgsConstructor
public class AddressDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;
    @Column(name = "street")
    private String street;

    public AddressDataSet(String street) {
        this.street = street;
    }
}
