package com.olim.gura.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    @NotEmpty
    @NotNull
    private String firstName;
    @NotNull
    @NotEmpty
    private String lastName;
    @NotNull
    @NotEmpty(message = "Email should not be empty")
    @Email
    @NotNull
    private String email;
    @NotEmpty(message = "Password should not be empty")
    @NotNull
    private String password;
    @NotEmpty
    @NotNull
    private String role;

}