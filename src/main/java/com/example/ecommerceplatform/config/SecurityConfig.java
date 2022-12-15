package com.example.ecommerceplatform.config;

import com.example.ecommerceplatform.services.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JpaUserDetailsService jpaUserDetailsService;

    public SecurityConfig(JpaUserDetailsService jpaUserDetailsService) {
        this.jpaUserDetailsService = jpaUserDetailsService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf()
                .disable()
                .userDetailsService(jpaUserDetailsService)
                .authorizeHttpRequests()
                .requestMatchers("/private").hasRole("ADMIN")
                .requestMatchers("/private/changePassword").hasRole("ADMIN")
                .requestMatchers("/private/passwordChanged").hasRole("ADMIN")
                .requestMatchers("/public").hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin()
                .successHandler(myAuthenticationSuccessHandler());

        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new AuthenticationSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
