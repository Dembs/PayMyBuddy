package com.paymybuddy.webapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

@Data
public class UserDTO {
    @Email
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            message = "Veuillez saisir un format mail valid")
    @NotBlank
    @NotNull
    private String email;

    /*@ToString.Exclude*/
    @Size(min=8, max = 50)
   /* @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "Le mot de passe doit contenir au moins une lettre majuscule et un caractère spécial")*/
    private String password;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Le nom d'utilisateur ne doit pas être vide")
    /*@Size(min=1, max=50)*/
    private String username;

    public UserDTO() {
    }

    public UserDTO(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}