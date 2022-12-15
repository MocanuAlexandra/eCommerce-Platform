package com.tw.ecommerceplatform.filters;

import com.tw.ecommerceplatform.models.SecurityUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

// Utility class for CustomAuthenticationFilter
public class CustomAuthenticationFilterUtils {

    // Method used to create access token
    protected Cookie createToken(String access_token) {
        // Create a cookie for the token
        Cookie tokenCookie = new Cookie("access_token", access_token);

        // Get the timestamp for now
        Instant now = Instant.now();

        // Get the time stamp for 24 hours from now
        Instant expireTime = now.plus(1, ChronoUnit.DAYS);

        // The difference will always be the same, the number of seconds in a day
        tokenCookie.setMaxAge((int) Duration.between(now, expireTime).getSeconds());

        return tokenCookie;
    }

    ;

    // Method used to redirect the user by role
    public void redirectUserByRole(SecurityUserDetails user, HttpServletResponse response) throws IOException {
        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            response.sendRedirect("/private");
        } else if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            response.sendRedirect("/public");
        }
    }
}
