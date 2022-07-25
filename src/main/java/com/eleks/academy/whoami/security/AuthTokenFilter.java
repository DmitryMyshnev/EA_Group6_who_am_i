package com.eleks.academy.whoami.security;

import com.eleks.academy.whoami.security.exception.NotAcceptableOauthException;
import com.eleks.academy.whoami.security.jwt.Jwt;
import com.eleks.academy.whoami.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";
    @Autowired
    private UserService userService;
    @Autowired
    private Jwt jwt;
    @Autowired
    private TokenBlackList tokenBlackList;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = parseRequestHeader(request);
            if (!token.isBlank() && jwt.validateJwtToken(token)) {
                var email = jwt.getEmailFromJwtToken(token);
                if (tokenBlackList.containsKey(email)) {
                    throw new NotAcceptableOauthException();
                }
                var userDetails = userService.loadUserByUsername(email);
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (NotAcceptableOauthException e) {

        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private String parseRequestHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
            return headerAuth.split(BEARER)[1].trim();
        }
        return "";
    }
}
