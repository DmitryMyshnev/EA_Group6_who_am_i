package com.eleks.academy.whoami.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenCommandDto {

    @NotBlank
    @NotNull
    private String refreshToken;
}
