package mvc.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import mvc.model.Person;

class DAOPersonTest {

    @TempDir
    Path tempDir;

    private DAOPerson dao;

    @BeforeEach
    void setUp() {
        dao = new DAOPerson(new File(tempDir.toFile(), "persons.xml"));
    }

    private static Person person(int id, String name) {
        Person p = new Person();
        p.setId(id);
        p.setName(name);
        p.setLastName("Apellido" + id);
        p.setEmail(name.toLowerCase() + "@example.com");
        p.setPhoneNumber("60000000" + id);
        return p;
    }

    @Test
    void readAll_isEmpty_whenFileDoesNotExist() {
        assertTrue(dao.readAll().isEmpty());
    }

    @Test
    void create_persistsPerson() {
        dao.create(person(1, "Ana"));

        var all = dao.readAll();
        assertEquals(1, all.size());
        assertEquals("Ana", all.get(0).getName());
        assertEquals("ana@example.com", all.get(0).getEmail());
    }

    @Test
    void create_appendsMultiplePeople() {
        dao.create(person(1, "Ana"));
        dao.create(person(2, "Beto"));

        assertEquals(2, dao.readAll().size());
    }

    @Test
    void read_returnsMatchingPerson() {
        dao.create(person(1, "Ana"));
        dao.create(person(2, "Beto"));

        Person query = new Person();
        query.setId(2);
        Person found = dao.read(query);

        assertNotNull(found);
        assertEquals("Beto", found.getName());
    }

    @Test
    void read_returnsNull_whenNotFound() {
        dao.create(person(1, "Ana"));

        Person query = new Person();
        query.setId(99);
        assertNull(dao.read(query));
    }

    @Test
    void update_modifiesExistingPerson() {
        dao.create(person(1, "Ana"));

        Person changed = person(1, "Ana María");
        assertTrue(dao.update(changed));

        Person query = new Person();
        query.setId(1);
        assertEquals("Ana María", dao.read(query).getName());
    }

    @Test
    void update_returnsFalse_whenPersonNotFound() {
        assertFalse(dao.update(person(42, "Nadie")));
    }

    @Test
    void delete_removesPerson() {
        dao.create(person(1, "Ana"));
        dao.create(person(2, "Beto"));

        assertTrue(dao.delete(person(1, "Ana")));

        var all = dao.readAll();
        assertEquals(1, all.size());
        assertEquals(2, all.get(0).getId());
    }

    @Test
    void delete_returnsFalse_whenPersonNotFound() {
        dao.create(person(1, "Ana"));

        assertFalse(dao.delete(person(99, "Fantasma")));
        assertEquals(1, dao.readAll().size());
    }

    @Test
    void loadFile_returnsEmptyRoot_whenFileMissing() {
        assertEquals("persons", dao.loadFile().getName());
        assertTrue(dao.loadFile().getChildren().isEmpty());
    }
}
