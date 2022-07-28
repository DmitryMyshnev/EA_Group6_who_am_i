package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.db.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    Optional<Theme> findByNameIgnoreCase(String name);
}
