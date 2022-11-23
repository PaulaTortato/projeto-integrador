package com.grupo6.projetointegrador.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grupo6.projetointegrador.model.enumeration.Active;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String address;

    @NotNull
    private int houseNumber;

    @NotNull
    private String zipCode;

    @Enumerated(EnumType.STRING)
    private Active active;

    @OneToMany(mappedBy = "seller")
    @JsonManagedReference
    private List<Product> products;
}