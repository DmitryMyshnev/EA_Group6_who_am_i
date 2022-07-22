package com.eleks.academy.whoami.db.dto;

import lombok.Data;

@Data
public class CreateUserCommand {

    private String name;

    private String email;

    private String password;
}
