package com.eleks.academy.whoami.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialRequest {
    @Email
    @NotNull
    private String email;

    @Length(min = 8)
    private String password;
}
