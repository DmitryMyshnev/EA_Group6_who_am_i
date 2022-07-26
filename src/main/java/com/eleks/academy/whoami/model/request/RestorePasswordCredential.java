package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestorePasswordCredential {

    @NotNull
    @Length(min = 8, message = "Password must be more than 8 symbols")
    @Pattern(regexp = "^[^\\s]+[^\\s]+$", message = "Password cannot have any whitespaces")
    private String newPassword;

    @NotNull
    private String confirmPassword;

    @NotNull
    private String confirmToken;
}
