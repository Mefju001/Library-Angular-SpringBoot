package com.app.library.Service;

import com.app.library.DTO.Request.UserDetailsRequest;
import com.app.library.DTO.Request.UserPasswordRequest;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Role;
import com.app.library.Entity.User;
import com.app.library.Repository.FavoritebooksRepository;
import com.app.library.Repository.RoleRepository;
import com.app.library.Repository.UserRepository;
import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Security.DTO.Response.JwtResponse;
import com.app.library.Security.DTO.Response.MessageResponse;
import com.app.library.Security.JWT.JwtUtils;
import com.app.library.Security.Service.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final FavoritebooksRepository favoritebooksRepository;

    private final AuthenticationManager authenticationManager;


    private final UserRepository userRepository;


    private final RoleRepository roleRepository;


    private final PasswordEncoder encoder;


    private final JwtUtils jwtUtils;
    @Autowired
    public UserService(FavoritebooksRepository favoritebooksRepository, AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.favoritebooksRepository = favoritebooksRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }
    public ResponseEntity<List<Favoritebooks>> findall() {
        try {
            List<Favoritebooks> favoritebooks = favoritebooksRepository.findAll();
            /*List<BookResponse>bookResponses = books.stream()
                    .map(bookMapper::toDto)
                    .toList();*/
            return new ResponseEntity<>(favoritebooks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<User> findbyid(Long id) {
        try {
            Optional<User> users = userRepository.findById(id);
            /*List<BookResponse>bookResponses = books.stream()
                    .map(bookMapper::toDto)
                    .toList();*/
            return users.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<JwtResponse> login(UserRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities()));

    }
    @Transactional
    public ResponseEntity<?> changedetails(Long id, UserDetailsRequest userRequest) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            User existingUser = user.get();

            // Aktualizacja pozostałych danych użytkownika (jeśli są dostarczone)
            if (!existingUser.getName().equals(userRequest.getName())&&userRequest.getName()!=null) {
                existingUser.setName(userRequest.getName());
            }
            if (!existingUser.getSurname().equals(userRequest.getSurname())&&userRequest.getSurname()!=null) {
                existingUser.setSurname(userRequest.getSurname());
            }
            if (!existingUser.getEmail().equals(userRequest.getEmail())&&userRequest.getEmail()!=null){
                existingUser.setEmail(userRequest.getEmail());
            }
            // Zapisz zmodyfikowanego użytkownika
            userRepository.save(existingUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Użytkownik o podanym ID nie istnieje", HttpStatus.NOT_FOUND);
        }
    }
    @Transactional
    public ResponseEntity<?> changepassword(Long id, UserPasswordRequest userPasswordRequest) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User existingUser = user.get();

            // 1. Sprawdzanie, czy stare hasło jest poprawne
            if (!encoder.matches(userPasswordRequest.getOldpassword(), existingUser.getPassword())) {
                return new ResponseEntity<>("Stare hasło jest niepoprawne", HttpStatus.BAD_REQUEST);
            }

            // 2. Sprawdzanie, czy nowe hasło nie jest takie samo jak stare
            if (userPasswordRequest.getOldpassword().equals(userPasswordRequest.getNewpassword())) {
                return new ResponseEntity<>("Nowe hasło nie może być takie samo jak stare", HttpStatus.BAD_REQUEST);
            }
            if (!userPasswordRequest.getNewpassword().equals(userPasswordRequest.getConfirmpassword())) {
                return new ResponseEntity<>("Hasła do siebie nie pasuja", HttpStatus.BAD_REQUEST);
            }
            // 3. Zaszyfrowanie nowego hasła
            existingUser.setPassword(encoder.encode(userPasswordRequest.getNewpassword()));
            userRepository.save(existingUser);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Użytkownik o podanym ID nie istnieje", HttpStatus.NOT_FOUND);
        }
    }
    @Transactional
    public ResponseEntity<?> registerUp(UserRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Username is taken"));
        }

        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRola("ROLE_USER").orElseThrow());

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User register!"));
    }
    @Transactional
    public ResponseEntity<Favoritebooks>deleteuser(Long id)
    {
        Optional<User> existingdata = userRepository.findById(id);
        if(existingdata.isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<Favoritebooks>addfavoritebooks(Favoritebooks favoritebooks)
    {
        favoritebooksRepository.save(favoritebooks);
        return new ResponseEntity<>(favoritebooks,HttpStatus.CREATED);
    }
    @Transactional
    public ResponseEntity<Favoritebooks>updatefavoritebooks(Favoritebooks favoritebooks)
    {
        Optional<Favoritebooks> existingdata = favoritebooksRepository.findById(favoritebooks.getId());
        if(existingdata.isPresent()) {
            Favoritebooks updatedata = existingdata.get();
            updatedata.setUser(existingdata.get().getUser());
            updatedata.setBook(existingdata.get().getBook());
            favoritebooksRepository.save(updatedata);
            return new ResponseEntity<>(updatedata, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Transactional
    public ResponseEntity<Favoritebooks>deletefavoritebooks(Integer id)
    {
        Optional<Favoritebooks> existingdata = favoritebooksRepository.findById(id);
        if(existingdata.isPresent()) {
            favoritebooksRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
