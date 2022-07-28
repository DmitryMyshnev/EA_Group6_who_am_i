package com.eleks.academy.whoami.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateLobbyCommandDto {

    @NotNull
    private String theme;

    @NotNull
    @Min(value = 4,message = "number of players must be more or equals 4")
    @Max(value = 12,message = "number of players must be less or equals 12")
    private Integer numberOfPlayers;

    private Boolean isPrivate;

    @Length(min = 8,message = "password must be more 8 symbols")
    @Pattern(regexp = "^[^\\s]+[^\\s]+$", message = "Password cannot have any whitespaces")
    private String password;

    @NotNull
    private Long userId;
}
