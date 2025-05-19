package auth.session.application.ports.output;

public interface SessionRepositoryPort {
    void saveSession(String token, String userId, long expirationTime);
    String getUserIdFromToken(String token);
    void deleteSession(String token);
}
