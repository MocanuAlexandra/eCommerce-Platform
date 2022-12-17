package com.tw.ecommerceplatform.services;


import com.tw.ecommerceplatform.models.RoleEntity;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Get the user from the database
        UserEntity user = userRepository.findByEmail(email);

        // If no user was found, throw UsernameNotFoundException
        if (user == null)
            throw new UsernameNotFoundException("No user with this username could be found");

        return new SecurityUserDetails(user);
    }

    public void changePassword(String email, String currentPassword, String newPassword) throws Exception {

        UserEntity user = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new Exception("Incorrect current password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void registerCustomer(String email, String newPassword, RoleEntity role) throws Exception {
        UserEntity savedUser = userRepository.findByEmail(email);

        if (savedUser != null) {
            throw new Exception("User already exists");
        } else {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(newPassword));
            newUser.setRole(role);

            userRepository.save(newUser);
        }
    }
}
