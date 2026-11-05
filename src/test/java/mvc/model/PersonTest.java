package mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PersonTest {

    private static Person withName(String name) {
        return new Person(1, name, "", "", "");
    }

    private static Person withEmail(String email) {
        return new Person(1, "Ana", "", email, "");
    }

    @Test
    void isValid_whenOnlyNameIsPresent() {
        assertTrue(withName("Ana").findValidationError().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "   " })
    void requiresName(String name) {
        assertEquals("El nombre es obligatorio.", withName(name).findValidationError().orElseThrow());
    }

    @Test
    void requiresName_whenNull() {
        assertEquals("El nombre es obligatorio.", withName(null).findValidationError().orElseThrow());
    }

    @ParameterizedTest
    @ValueSource(strings = { "ana@example.com", "ana.perez+dir@sub.example.mx" })
    void acceptsValidEmail(String email) {
        assertTrue(withEmail(email).findValidationError().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "ana", "ana@", "@example.com", "ana@example", "ana perez@example.com", "a@b@c.com" })
    void rejectsInvalidEmail(String email) {
        assertEquals("El correo no tiene un formato válido.", withEmail(email).findValidationError().orElseThrow());
    }
}
