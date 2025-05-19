package auth.session;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {

    @Test
    public void testBcrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newHash = encoder.encode("admin123");
        System.out.println("Nuevo hash: " + newHash);

        boolean matches = encoder.matches("admin123", "$2a$10$N9qo8uLOickgx2ZMRZoMy.MrE7LHVQ6W7Gq6hZ6Kj7Qf7QJQ1q1O");
        System.out.println("¿Coincide? " + matches);
    }
    @Test
    public void debugHashStructure() {
        String storedHash = "$2a$10$N9qo8uLOickgx2ZMRZoMy.MrE7LHVQ6W7Gq6hZ6Kj7Qf7QJQ1q1O";

        // Verifica el prefijo (debe empezar con $2a$)
        System.out.println("Prefijo: " + storedHash.substring(0, 4));

        // Verifica el costo (debe ser $10$)
        System.out.println("Costo: " + storedHash.substring(4, 7));

        // Verifica el resto del hash
        System.out.println("Salt + hash: " + storedHash.substring(7));
    }


    @Test
    public void replaceProblematicHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 1. Generar nuevo hash válido
        String newValidHash = encoder.encode("admin123");
        System.out.println("NUEVO HASH VÁLIDO: " + newValidHash);

        // 2. Verificar que funciona
        boolean check = encoder.matches("admin123", newValidHash);
        System.out.println("VERIFICACIÓN NUEVO HASH: " + check); // Debe ser true

        // 3. Actualizar tu base de datos con ESTE hash
        System.out.println("\nINSTRUCCIÓN SQL PARA ACTUALIZAR:");
        System.out.println("UPDATE usuarios SET password = '" + newValidHash +
                "' WHERE username = 'admin';");
    }



}