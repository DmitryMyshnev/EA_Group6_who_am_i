package com.eleks.academy.whoami.db.mapper;

import com.eleks.academy.whoami.db.dto.ThemeDto;
import com.eleks.academy.whoami.db.model.Theme;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ThemeMapper {

    ThemeDto toDto(Theme theme);
}
