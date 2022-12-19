package com.tw.ecommerceplatform.services;


import com.tw.ecommerceplatform.utility.RegistrationStatus;
import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.entities.SecurityUserDetails;
import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.models.RegisterUserModel;
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
    private final RoleService roleService;

    public JpaUserDetailsService(UserRepository userRepository,
                                 @Lazy PasswordEncoder passwordEncoder,
                                 RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Get the user from the database
        UserEntity user = userRepository.findByEmail(email);

        // If no user was found, throw UsernameNotFoundException
        if (user == null)
            throw new UsernameNotFoundException("No user with this email could be found");

        return new SecurityUserDetails(user);
    }

    // Change password
    public void changePassword(String email, String currentPassword, String newPassword) throws Exception {

        UserEntity user = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new Exception("Incorrect current password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    // Register user

    public UserEntity registerUser(RegisterUserModel registerUserModel, RoleEntity role) throws Exception {
        UserEntity savedUser = userRepository.findByEmail(registerUserModel.getUsername());
        UserEntity newUser = new UserEntity();

        // Check if the user already exists
        if (savedUser != null) {
            throw new Exception("User already exists");
        } else {
            // Check if user has role of customer - if yes, set registration status to approved
            if (role == roleService.getRoleByName("CUSTOMER")) {
                newUser.setEmail(registerUserModel.getUsername());
                newUser.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
                newUser.setRole(role);
                newUser.setStatus(RegistrationStatus.APPROVED);

                // if not, set registration status to pending
            } else{
                newUser.setEmail(registerUserModel.getUsername());
                newUser.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
                newUser.setRole(role);
                newUser.setStatus(RegistrationStatus.PENDING);
            }
            userRepository.save(newUser);
        }
        return newUser;
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
