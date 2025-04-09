package com.app.library.Service;

import com.app.library.DTO.Mapper.FavoriteBooksMapper;
import com.app.library.DTO.Request.UserDetailsRequest;
import com.app.library.DTO.Request.UserPasswordRequest;
import com.app.library.DTO.Response.FavoriteBooksResponse;
import com.app.library.DTO.Response.UserResponse;
import com.app.library.Entity.Book;
import com.app.library.Entity.Favoritebooks;
import com.app.library.Entity.Role;
import com.app.library.Entity.User;
import com.app.library.Repository.BookRepository;
import com.app.library.Repository.FavoritebooksRepository;
import com.app.library.Repository.RoleRepository;
import com.app.library.Repository.UserRepository;
import com.app.library.Security.DTO.Request.UserRequest;
import com.app.library.Security.DTO.Response.JwtResponse;
import com.app.library.Security.JWT.JwtUtils;
import com.app.library.Security.Service.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final FavoritebooksRepository favoritebooksRepository;

    private final AuthenticationManager authenticationManager;


    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final RoleRepository roleRepository;
    private final FavoriteBooksMapper favoriteBooksMapper;
    private final PasswordEncoder encoder;


    private final JwtUtils jwtUtils;
    @Autowired
    public UserService(FavoritebooksRepository favoritebooksRepository, AuthenticationManager authenticationManager, UserRepository userRepository, BookRepository bookRepository, RoleRepository roleRepository, FavoriteBooksMapper favoriteBooksMapper, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.favoritebooksRepository = favoritebooksRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.roleRepository = roleRepository;
        this.favoriteBooksMapper = favoriteBooksMapper;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }
    public List<FavoriteBooksResponse> findall(Long userId) {
            List<Favoritebooks> favoritebooks = favoritebooksRepository.findFavoritebooksByUser_Id(userId);
        return favoritebooks.stream()
                    .map(favoriteBooksMapper::toDto)
                    .toList();
    }
    public UserResponse findbyid(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getName())
                .password(user.getPassword())
                .role(String.valueOf(user.getRoles()))
                .build();
    }
    public JwtResponse login(UserRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities());

    }
    @Transactional
    public void changedetails(Long id, UserDetailsRequest userRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik o podanym ID nie istnieje"));

        if (userRequest.getName() != null && !existingUser.getName().equals(userRequest.getName())) {
            existingUser.setName(userRequest.getName());
        }
        if (userRequest.getSurname() != null && !existingUser.getSurname().equals(userRequest.getSurname())) {
            existingUser.setSurname(userRequest.getSurname());
        }
        if (userRequest.getEmail() != null && !existingUser.getEmail().equals(userRequest.getEmail())) {
            existingUser.setEmail(userRequest.getEmail());
        }

        userRepository.save(existingUser);
    }
    @Transactional
    public void changepassword(Long id, UserPasswordRequest userPasswordRequest) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Użytkownik o podanym ID nie istnieje"));

        // 1. Sprawdzanie, czy stare hasło jest poprawne
        if (!encoder.matches(userPasswordRequest.getOldpassword(), existingUser.getPassword())) {
            throw new IllegalArgumentException("Stare hasło jest niepoprawne");
        }

        // 2. Sprawdzanie, czy nowe hasło nie jest takie samo jak stare
        if (userPasswordRequest.getOldpassword().equals(userPasswordRequest.getNewpassword())) {
            throw new IllegalArgumentException("Nowe hasło nie może być takie samo jak stare");
        }

        // 3. Sprawdzanie, czy nowe hasła pasują do siebie
        if (!userPasswordRequest.getNewpassword().equals(userPasswordRequest.getConfirmpassword())) {
            throw new IllegalArgumentException("Hasła do siebie nie pasują");
        }

        // 4. Zaszyfrowanie nowego hasła
        existingUser.setPassword(encoder.encode(userPasswordRequest.getNewpassword()));
        userRepository.save(existingUser);
    }
    @Transactional
    public void registerUp(UserRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Username is taken");
        }

        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRola("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found")));

        user.setRoles(roles);
        userRepository.save(user);
    }
    @Transactional
    public void deleteuser(Long id)
    {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public FavoriteBooksResponse addfavoritebooks(Integer bookId, Long userId)
    {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        boolean exists = favoritebooksRepository.existsByBookAndUser(book, user);
        if (exists) {
            throw new IllegalArgumentException("is exist");
        }

        Favoritebooks favoritebooks = new Favoritebooks(book,user);
        favoritebooksRepository.save(favoritebooks);

        return  FavoriteBooksResponse.builder()
                .id(favoritebooks.getId())
                .title(favoritebooks.getBook().getTitle())
                .authorName(favoritebooks.getBook().getAuthor().getName())
                .authorSurname(favoritebooks.getBook().getAuthor().getSurname())
                .publicationDate(favoritebooks.getBook().getpublicationDate())
                .isbn(favoritebooks.getBook().getIsbn())
                .genreName(favoritebooks.getBook().getGenre().getName())
                .language(favoritebooks.getBook().getLanguage())
                .publisherName(favoritebooks.getBook().getPublisher().getName())
                .pages(favoritebooks.getBook().getPages())
                .price(favoritebooks.getBook().getPrice())
                .build();
    }
    @Transactional
    public Favoritebooks updatefavoritebooks(Favoritebooks favoritebooks)
    {
        Favoritebooks existingdata = favoritebooksRepository.findById(favoritebooks.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existingdata.setUser(existingdata.getUser());
        existingdata.setBook(existingdata.getBook());
        favoritebooksRepository.save(existingdata);
        return existingdata;

    }
    @Transactional
    public void deletefavoritebooks(Integer id)
    {
        Favoritebooks existingdata = favoritebooksRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
            favoritebooksRepository.delete(existingdata);
    }
}
