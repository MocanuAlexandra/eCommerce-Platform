package com.tw.ecommerceplatform.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.tw.ecommerceplatform.models.SecurityUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

//TODO DELETE COOKIE ON "/logout"
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    CustomAuthenticationFilterUtils customAuthenticationFilterUtils = new CustomAuthenticationFilterUtils();

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // Method called when a user tries to log in
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    // Method called when a user successfully logs in
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // TODO CHANGE SECRET
        SecurityUserDetails user = (SecurityUserDetails) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        // Create access token
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        // Create refresh token
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        // Add the tokens in headers
        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);

        // Add the tokens in cookies
        response.addCookie(customAuthenticationFilterUtils.createToken(access_token));

        // If the user has successfully authenticated, redirect him by role to the appropriate page
        customAuthenticationFilterUtils.redirectUserByRole(user, response);
    }

    // Method called when a user fails to log in
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.sendRedirect("/login?error");
    }
}
