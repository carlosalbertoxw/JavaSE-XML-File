package mvc.model;

import java.util.Optional;
import java.util.regex.Pattern;

public record Person(int id, String name, String lastName, String email, String phoneNumber) {

    private static final Pattern EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");

    /** Devuelve el primer problema encontrado en los datos, o vacío si son válidos. */
    public Optional<String> findValidationError() {
        if (name == null || name.isBlank()) {
            return Optional.of("El nombre es obligatorio.");
        }
        if (email != null && !email.isBlank() && !EMAIL.matcher(email).matches()) {
            return Optional.of("El correo no tiene un formato válido.");
        }
        return Optional.empty();
    }
}
