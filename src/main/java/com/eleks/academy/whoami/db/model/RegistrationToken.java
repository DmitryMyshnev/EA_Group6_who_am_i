package com.eleks.academy.whoami.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "registration_token")
public class RegistrationToken {

    @Id
    private String token;

    @Column(name = "create_time")
    private Long createTime;
}
