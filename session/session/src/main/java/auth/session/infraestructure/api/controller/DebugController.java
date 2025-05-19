package auth.session.infraestructure.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private final PasswordEncoder passwordEncoder;

    public DebugController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/bcrypt")
    public ResponseEntity<String> testBcrypt(@RequestParam String password) {
        String encoded = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, encoded);
        boolean matchesStored = passwordEncoder.matches(password, "$2a$10$N9qo8uLOickgx2ZMRZoMy.MrE7LHVQ6W7Gq6hZ6Kj7Qf7QJQ1q1O");

        return ResponseEntity.ok(
                "Password: " + password + "\n" +
                        "Encoded: " + encoded + "\n" +
                        "Matches new: " + matches + "\n" +
                        "Matches stored: " + matchesStored
        );
    }
}
