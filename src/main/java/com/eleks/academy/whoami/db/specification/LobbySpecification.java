package com.eleks.academy.whoami.db.specification;

import com.eleks.academy.whoami.db.model.Lobby;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LobbySpecification {

    public static Specification<Lobby> themeIn(List<String> themes) {
        return (root, query, builder) -> builder.in(root.get("theme").get("name")).value(themes);
    }
}
