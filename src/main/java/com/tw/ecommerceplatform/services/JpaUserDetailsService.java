package com.tw.ecommerceplatform.services;


import com.tw.ecommerceplatform.models.SecurityUserDetails;
import com.tw.ecommerceplatform.models.UserEntity;
import com.tw.ecommerceplatform.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public JpaUserDetailsService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Get the user from the database
        UserEntity user = userRepository.findByUsername(username);

        // If no user was found, throw UsernameNotFoundException
        if (user == null)
            throw new UsernameNotFoundException("No user with this username could be found");

        return new SecurityUserDetails(user);
    }

    public void save(UserEntity savedUser) {
        UserEntity user = userRepository.findByUsername(savedUser.getUsername());

        // verify if user exists
        if (user != null) {
            user.setUsername(savedUser.getUsername());
            user.setPassword(passwordEncoder.encode(savedUser.getPassword()));
        }

        // save user if not exists //TODO: AT REGISTER
        assert user != null;
        userRepository.save(user);
    }
}
