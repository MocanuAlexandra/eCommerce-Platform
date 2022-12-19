package com.tw.ecommerceplatform.services;


import com.tw.ecommerceplatform.Utility.RegistrationStatus;
import com.tw.ecommerceplatform.entities.RoleEntity;
import com.tw.ecommerceplatform.entities.SecurityUserDetails;
import com.tw.ecommerceplatform.entities.UserEntity;
import com.tw.ecommerceplatform.models.RegisterUserModel;
import com.tw.ecommerceplatform.repositories.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public JpaUserDetailsService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                                 RoleRepository roleRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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

    public void changePassword(String email, String currentPassword, String newPassword) throws Exception {

        UserEntity user = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new Exception("Incorrect current password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public UserEntity registerUser(RegisterUserModel registerUserModel, RoleEntity role) throws Exception {
        UserEntity savedUser = userRepository.findByEmail(registerUserModel.getUsername());
        UserEntity newUser = new UserEntity();

        if (savedUser != null) {
            throw new Exception("User already exists");
        } else {
            if(role==roleService.getRoleByName("CUSTOMER")) {
                newUser.setEmail(registerUserModel.getUsername());
                newUser.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
                newUser.setRole(role);
                newUser.setStatus(RegistrationStatus.APPROVED);
            }
            else if(role==roleService.getRoleByName("WAREHOUSE_ADMIN"))
            {
                newUser.setEmail(registerUserModel.getUsername());
                newUser.setPassword(passwordEncoder.encode(registerUserModel.getPassword()));
                newUser.setRole(role);
                newUser.setStatus(RegistrationStatus.PENDING);
            }
            userRepository.save(newUser);
        }
        return newUser;
    }

    public void deleteUser(Long id)
    {
        userRepository.deleteById(id);
    }
}
