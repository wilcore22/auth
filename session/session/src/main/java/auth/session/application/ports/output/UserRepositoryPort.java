package auth.session.application.ports.output;

import auth.session.domain.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);  // Método añadido
    User save(User user);
}
