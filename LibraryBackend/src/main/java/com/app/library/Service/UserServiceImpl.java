package com.app.library.Service;

import com.app.library.DTO.Mapper.FavoriteBooksMapper;
import com.app.library.DTO.Mapper.UserMapper;
import com.app.library.DTO.Request.UserDetailsRequest;
import com.app.library.DTO.Request.UserPasswordRequest;
import com.app.library.DTO.Response.BookResponse;
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
import com.app.library.Security.Service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
public class UserServiceImpl implements UserService {
    private final FavoritebooksRepository favoritebooksRepository;

    private final AuthenticationManager authenticationManager;


    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final RoleRepository roleRepository;
    private final FavoriteBooksMapper favoriteBooksMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final UserDetailsServiceImpl userDetailsServiceImpl;


    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(FavoritebooksRepository favoritebooksRepository, AuthenticationManager authenticationManager, UserRepository userRepository, BookRepository bookRepository, RoleRepository roleRepository, FavoriteBooksMapper favoriteBooksMapper, UserMapper userMapper, PasswordEncoder encoder, UserDetailsServiceImpl userDetailsServiceImpl, JwtUtils jwtUtils) {
        this.favoritebooksRepository = favoritebooksRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.roleRepository = roleRepository;
        this.favoriteBooksMapper = favoriteBooksMapper;
        this.userMapper = userMapper;
        this.encoder = encoder;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public List<FavoriteBooksResponse> findAllLikedBooks(Long userId) {
        List<Favoritebooks> favoritebooks = favoritebooksRepository.findFavoritebooksByUser_Id(userId);
        return favoritebooks.stream()
                .map(favoriteBooksMapper::toDto)
                .toList();
    }

    @Override
    public List<UserResponse> findAll() {
        List<User> users = userRepository.findUsersByRole("Role_User");
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserResponse findbyid(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getName())
                .password(user.getPassword())
                .role(String.valueOf(user.getRoles()))
                .build();
    }
    public JwtResponse refreshToken(HttpServletRequest request,HttpServletResponse response) {
        String RefreshToken = null;
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    RefreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (RefreshToken == null || !jwtUtils.validateJwtRefreshToken(RefreshToken)) {
            return null;
        }

        try {
            String username = jwtUtils.getUserNameFromJwtRefreshToken(RefreshToken);

            // 4. Załaduj UserDetails (użytkownika) - Spring Security potrzebuje pełnych danych
            UserDetailsImpl userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            // 5. Wygeneruj nowy access token
            // Użyj istniejącej logiki generowania tokena dostępu, być może z Authentication
            // Możesz stworzyć tymczasowy obiekt Authentication lub przekazać UserDetails
            // Jeśli Twoja metoda generateJwtToken przyjmuje UserDetails, to będzie prościej:
            // String newAccessToken = jwtUtils.generateJwtToken(userDetails);
            // Jeśli przyjmuje Authentication, musisz stworzyć nowy obiekt Authentication:
            // Uwaga: Tutaj nie ma uwierzytelniania hasłem, tylko na podstawie ważności tokena odświeżania.
            // Poniżej uproszczony przykład, możesz potrzebować bardziej złożonej logiki.
            String newAccessToken = jwtUtils.generateTokenFromUsername(username); // Przyjmij, że masz taką metodę w JwtUtils

            // Opcjonalnie: Rotacja refresh tokena
            // Możesz wygenerować nowy refresh token i ustawić nowe ciasteczko,
            // aby zwiększyć bezpieczeństwo i przedłużyć sesję.
            // String newRefreshToken = jwtUtils.generateRefreshToken(userDetails); // lub newRefreshToken(username)
            // ResponseCookie newRefreshTokenCookie = ResponseCookie.from("refresh_token", newRefreshToken)
            //         .httpOnly(true)
            //         .secure(true) // Pamiętaj o HTTPS w produkcji!
            //         .path("/")
            //         .maxAge(jwtUtils.getJwtRefreshExpirationMs() / 1000)
            //         .sameSite("Lax")
            //         .build();
            // response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());

            // 6. Zwróć nowy access token w ciele odpowiedzi

            return new JwtResponse(newAccessToken,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getAuthorities());

        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public JwtResponse login(UserRequest loginRequest, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String RefreshToken = jwtUtils.generateRefreshToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie refreshTokenCookie  = ResponseCookie.from("refresh_token", RefreshToken)
                .httpOnly(true)
                .secure(false) // Pamiętaj: true w produkcji!
                .path("/")
                .maxAge(jwtUtils.getJwtRefreshExpirationMs() / 1000)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getAuthorities());

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
    @Transactional
    public FavoriteBooksResponse addfavoritebooks(Integer bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        boolean exists = favoritebooksRepository.existsByBookAndUser(book, user);
        if (exists) {
            throw new IllegalArgumentException("is exist");
        }

        Favoritebooks favoritebooks = new Favoritebooks(book, user);
        favoritebooksRepository.save(favoritebooks);

        return FavoriteBooksResponse.builder()
                .id(favoritebooks.getId())
                .book(new BookResponse(favoritebooks.getBook().getId(), favoritebooks.getBook().getTitle(), favoritebooks.getBook().getAuthor().getName(), favoritebooks.getBook().getAuthor().getSurname(), favoritebooks.getBook().getpublicationDate(),
                        favoritebooks.getBook().getIsbn(), favoritebooks.getBook().getGenre().getName(), favoritebooks.getBook().getLanguage(), favoritebooks.getBook().getPublisher().getName(), favoritebooks.getBook().getPages(), favoritebooks.getBook().getPrice()))
                .build();
    }

    @Override
    @Transactional
    public Favoritebooks updatefavoritebooks(Favoritebooks favoritebooks) {
        Favoritebooks existingdata = favoritebooksRepository.findById(favoritebooks.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existingdata.setUser(existingdata.getUser());
        existingdata.setBook(existingdata.getBook());
        favoritebooksRepository.save(existingdata);
        return existingdata;

    }

    @Override
    @Transactional
    public void deletefavoritebooks(Integer id) {
        Favoritebooks existingdata = favoritebooksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
        favoritebooksRepository.delete(existingdata);
    }

    @Override
    public Long getUserCount() {
        return userRepository.countUsersByRoleName("ROLE_USER");
    }
}
