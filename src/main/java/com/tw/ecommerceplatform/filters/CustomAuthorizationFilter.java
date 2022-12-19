package com.tw.ecommerceplatform.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CustomAuthorizationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // First check if the request path is for login
        // if it is, we don't want to do anything
        if (request.getServletPath().equals("/login"))
            filterChain.doFilter(request, response);
        else {
            // Try to get the token from a cookie
            String token = getTokenFromCookie(request);

            // If the cookie was not found, try to get the token from the authorization header
            if (token == null)
                token = getTokenFromAuthorizationHeader(request);

            // If the token was found, perform verification
            if (token != null) {
                try {
                    // Let the request go forward if the token was successfully verified
                    if (verifyToken(token))
                        filterChain.doFilter(request, response);
                } catch (Exception e) {
                    // If an error has occurred, reject the request
                    response.sendError(FORBIDDEN.value());
                }
            } else {
                // The token was not found, move forward
                filterChain.doFilter(request, response);
            }
        }
    }

    // Decodes and checks the token, returns true if all went well
    // and it throws an exception if not
    private boolean verifyToken(String token) {
        // Decode the token
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        // Get the username and roles from the decoded token
        String username = decodedJWT.getSubject();

        String[] roles = decodedJWT.getClaim("role").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        // Create an authentication token and pass it to the security context holder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return true;
    }

    // Checks if the request has an authorization header and retrieves the jwt token from it
    // Returns the token if it is found and null otherwise
    private String getTokenFromAuthorizationHeader(HttpServletRequest request) {
        // Get the authorization header
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        // Check if the authorization header exists and has the right format
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // we get the token by cutting out the "Bearer " prefix
            return authorizationHeader.substring("Bearer ".length());
        }

        return null;
    }


    // Checks if the request has a cookie names "access_token" and retrieves the jwt token from it
    // Returns the token if it is found and null otherwise
    private String getTokenFromCookie(HttpServletRequest request) {
        try {// Get the cookies from the request and filter them to find the one
            // names "access_token", if it doesn't exist, assign null
            Cookie accessTokenCookie = stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("access_token"))
                    .findFirst()
                    .orElse(null);

            // If the cookie was found, return its value
            if (accessTokenCookie != null)
                return accessTokenCookie.getValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
