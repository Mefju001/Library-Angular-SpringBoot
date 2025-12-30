package com.app.library.Service;

import com.app.library.DTO.Mapper.UserMapper;
import com.app.library.DTO.Request.UserDetailsRequest;
import com.app.library.DTO.Request.UserPasswordRequest;
import com.app.library.DTO.Response.UserResponse;
import com.app.library.Entity.Role;
import com.app.library.Entity.User;
import com.app.library.Repository.UserRepository;
import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Service.Interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest signUpRequest,Set<Role> roles) {
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));
        user.setRoles(roles);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findUsersByRole("Role_User");
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User findByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("User not found with name: " + name));
    }
    @Override
    public UserResponse findbyid(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return new UserResponse(user.getId(), user.getName(),String.valueOf(user.getRoles()));
    }

    @Override
    @Transactional
    public void changedetails(Long id, UserDetailsRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik o podanym ID nie istnieje"));

        if (userRequest.name() != null && !existingUser.getName().equals(userRequest.name())) {
            existingUser.setName(userRequest.name());
        }
        if (userRequest.surname() != null && !existingUser.getSurname().equals(userRequest.surname())) {
            existingUser.setSurname(userRequest.surname());
        }
        if (userRequest.email() != null && !existingUser.getEmail().equals(userRequest.email())) {
            existingUser.setEmail(userRequest.email());
        }

        userRepository.save(existingUser);
    }

    @Override
    public User getInternalUserEntity(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void changepassword(Long id, UserPasswordRequest userPasswordRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik o podanym ID nie istnieje"));

        // 1. Sprawdzanie, czy stare hasło jest poprawne
        if (!encoder.matches(userPasswordRequest.oldpassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("Stare hasło jest niepoprawne");
        }

        // 2. Sprawdzanie, czy nowe hasło nie jest takie samo jak stare
        if (userPasswordRequest.oldpassword().equals(userPasswordRequest.newpassword())) {
            throw new IllegalArgumentException("Nowe hasło nie może być takie samo jak stare");
        }

        // 3. Sprawdzanie, czy nowe hasła pasują do siebie
        if (!userPasswordRequest.newpassword().equals(userPasswordRequest.confirmpassword())) {
            throw new IllegalArgumentException("Hasła do siebie nie pasują");
        }

        // 4. Zaszyfrowanie nowego hasła
        existingUser.setPassword(encoder.encode(userPasswordRequest.newpassword()));
        userRepository.save(existingUser);
    }


    @Override
    @Transactional
    public void deleteuser(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    @Override
    public Long getUserCount() {
        return userRepository.countUsersByRoleName("ROLE_USER");
    }
}
