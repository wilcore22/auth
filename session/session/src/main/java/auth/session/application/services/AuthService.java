package auth.session.application.services;

import auth.session.domain.User;

public interface AuthService {
    String authenticate(String username, String password);
    void logout(String token);
    User getUserFromToken(String token);
}
