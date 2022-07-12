package com.eleks.academy.whoami.db.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class CreateUserCommandDto {

    @NotNull
    @NotBlank
    @Length(min =  2, max = 50, message = "Username is too short or too long")
    private String userName;

    @Email(regexp = "[\\w^@]+@\\S+\\w$", message = "Email is not correct")
    @NotBlank
    private String  email;

    @NotNull
    @Length(min = 8, message = "Password must be more than 8 characters")
    @Pattern(regexp = "^[^\\s]+[^\\s]+$", message = "Password cannot have any whitespaces")
    private String password;
}
