package auth.session.application.services;

import auth.session.application.ports.output.SessionRepositoryPort;
import auth.session.application.ports.output.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import auth.session.domain.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryPort userRepository;
    private final SessionRepositoryPort sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey jwtSecretKey;
    private final long jwtExpirationMs;

    public AuthServiceImpl(UserRepositoryPort userRepository,
                           SessionRepositoryPort sessionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        this.jwtExpirationMs = 86400000; // 24 horas
    }


    @Override
    public String authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> {
                    System.out.println("Contraseña ingresada: " + password);
                    System.out.println("Hash almacenado: " + user.getPassword());
                    System.out.println("Longitud hash: " + user.getPassword().length());

                    boolean matches = passwordEncoder.matches(password, user.getPassword());
                    System.out.println("Resultado comparación: " + matches);

                    return matches;
                })
                .map(user -> {
                    String token = generateToken(user);
                    sessionRepository.saveSession(token, user.getId().toString(), jwtExpirationMs);
                    return token;
                })
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }


    @Override
    public void logout(String token) {
        sessionRepository.deleteSession(token);
    }

    @Override
    public User getUserFromToken(String token) {
        String userId = sessionRepository.getUserIdFromToken(token);
        return userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(jwtSecretKey)
                .compact();
    }
}
