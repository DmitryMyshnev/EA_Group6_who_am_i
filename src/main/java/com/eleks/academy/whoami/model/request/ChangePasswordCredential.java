package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordCredential {

    @NotNull
    @NotBlank
    private String oldPassword;

    @NotNull
    @Length(min = 8)
    private String newPassword;

    @NotNull
    private String confirmPassword;

}
