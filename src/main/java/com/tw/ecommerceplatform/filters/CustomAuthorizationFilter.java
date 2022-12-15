package com.tw.ecommerceplatform.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    CustomAuthorizationFilterUtils customAuthorizationFilterUtils = new CustomAuthorizationFilterUtils();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // First check if the request path is for login
        // if it is, we don't want to do anything
        if (request.getServletPath().equals("/login"))
            filterChain.doFilter(request, response);
        else {
            // Try to get the token from a cookie
            String token = customAuthorizationFilterUtils.getTokenFromCookie(request);

            // If the cookie was not found, try to get the token from the authorization header
            if (token == null)
                token = customAuthorizationFilterUtils.getTokenFromAuthorizationHeader(request);

            // If the token was found, perform verification
            if (token != null) {
                try {
                    // Let the request go forward if the token was successfully verified
                    if (customAuthorizationFilterUtils.verifyToken(token))
                        filterChain.doFilter(request, response);
                } catch (Exception e) {
                    // If an error has occurred, let the user know what it is
                    response.setHeader("error", e.getMessage());
                    response.sendError(FORBIDDEN.value());
                }
            } else {
                // The token was not found, move forward
                filterChain.doFilter(request, response);
            }
        }
    }
}
