package com.eleks.academy.whoami.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsernameRequest {

    @Length(min = 2, max = 50, message = "Nickname too short or to long")
    private String username;
}
