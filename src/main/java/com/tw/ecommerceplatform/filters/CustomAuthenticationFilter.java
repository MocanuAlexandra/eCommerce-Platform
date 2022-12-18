package com.tw.ecommerceplatform.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tw.ecommerceplatform.entities.SecurityUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private String failRedirectURL = "/login";

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        SecurityUserDetails user = (SecurityUserDetails) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        // Create access token
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        // Create a cookie for the token
        Cookie tokenCookie = new Cookie("access_token", access_token);

        // Get the timestamp for now
        Instant now = Instant.now();

        // Get the time stamp for 24 hours from now
        Instant expireTime = now.plus(1, ChronoUnit.DAYS);

        // The difference will always be the same, the number of seconds in a day
        tokenCookie.setMaxAge((int) Duration.between(now, expireTime).getSeconds());
        response.addCookie(tokenCookie);


        // If the user has successfully authenticated, redirect him
        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            response.sendRedirect("/private");
        } else if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            response.sendRedirect("/public");
        }
        else if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_WAREHOUSE_ADMIN"))) {
            response.sendRedirect("/warehouse");
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.sendRedirect(failRedirectURL);
    }

    public String getFailRedirectURL() {
        return failRedirectURL;
    }

    public void setFailRedirectURL(String failRedirectURL) {
        this.failRedirectURL = failRedirectURL;
    }
}
