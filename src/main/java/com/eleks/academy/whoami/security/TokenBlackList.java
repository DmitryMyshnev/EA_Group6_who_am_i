package com.eleks.academy.whoami.security;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlackList extends ConcurrentHashMap<Long, String> {

    public TokenBlackList() {
        super();
    }
}
