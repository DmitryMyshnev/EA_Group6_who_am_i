package com.eleks.academy.whoami.db.dto;

import lombok.Data;

@Data
public class UserDto {

    private  Long id;

    private String name;

    private String email;

    private boolean isActivated;

}
