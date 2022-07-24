package com.eleks.academy.whoami.db.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "themes")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

   /* ACTORS,
    SINGERS,
    ASTRONAUTS,
    SCIENTISTS,
    POLITICIANS,
    HISTORICAL_FIGURES,
    SUPER_HEROES,
    MOVIE_CHARACTER,
    CARTOON_CHARACTER*/
}
