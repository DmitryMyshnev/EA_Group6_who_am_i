package com.eleks.academy.whoami.db.specification;

import com.eleks.academy.whoami.db.dto.LobbyFilter;
import com.eleks.academy.whoami.db.model.Lobby;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LobbySpecification {

    public static Specification<Lobby> createSpecification(LobbyFilter filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!filter.getThemeFilters().isEmpty()) {
                predicates.add(builder.in(root.get("theme").get("name")).value(filter.getThemeFilters()));
            }
            if (!filter.getCountPlayersFilters().isEmpty()) {
                predicates.add(builder.in(root.get("numberOfPlayers")).value(filter.getCountPlayersFilters()));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
