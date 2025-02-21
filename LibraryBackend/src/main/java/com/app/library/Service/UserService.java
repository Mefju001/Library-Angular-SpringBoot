package com.app.library.Service;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public ResponseEntity<JwtResponse> login(UserRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername()));

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
