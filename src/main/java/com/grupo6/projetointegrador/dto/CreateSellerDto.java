package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSellerDto {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Caracteres inválidos.")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Caracteres inválidos.")
    private String lastName;

    @NotNull
    @Pattern(regexp = "^[^\\s@]+@[a-z]+\\.com(\\.br)?$", message = "Email deve ser um email válido.")
    private String email;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Caracteres inválidos.")
    private String address;

    @NotNull
    private Integer houseNumber;

    @NotNull
    @Pattern(regexp = "^[0-9]*$")
    @Size(min = 8, max = 8)
    private String zipCode;
}
