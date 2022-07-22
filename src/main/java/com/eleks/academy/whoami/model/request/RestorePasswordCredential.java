package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestorePasswordCredential {

    @NotNull
    private String newPassword;

    @NotNull
    private String confirmPassword;

    @NotNull
    private String confirmToken;
}
