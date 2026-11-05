package mvc.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import mvc.model.Person;

class DAOPersonTest {

    @TempDir
    Path tempDir;

    private Path xmlFile;
    private DAOPerson dao;

    @BeforeEach
    void setUp() {
        xmlFile = tempDir.resolve("persons.xml");
        dao = new DAOPerson(xmlFile);
    }

    private static Person person(String name) {
        return new Person(0, name, "Apellido", name.toLowerCase() + "@example.com", "600000000");
    }

    @Test
    void readAll_isEmpty_whenFileDoesNotExist() {
        assertTrue(dao.readAll().isEmpty());
    }

    @Test
    void readAll_isEmpty_whenFileIsEmpty() throws IOException {
        Files.createFile(xmlFile);

        assertTrue(dao.readAll().isEmpty());
    }

    @Test
    void readAll_doesNotCreateFile() {
        dao.readAll();

        assertFalse(Files.exists(xmlFile));
    }

    @Test
    void create_persistsPerson() {
        dao.create(person("Ana"));

        List<Person> all = new DAOPerson(xmlFile).readAll();
        assertEquals(1, all.size());
        assertEquals("Ana", all.get(0).name());
        assertEquals("ana@example.com", all.get(0).email());
    }

    @Test
    void create_assignsConsecutiveIds() {
        assertEquals(1, dao.create(person("Ana")).id());
        assertEquals(2, dao.create(person("Beto")).id());
    }

    @Test
    void create_ignoresReceivedId_andDoesNotDuplicate() {
        Person first = dao.create(person("Ana"));

        Person second = dao.create(first);

        assertEquals(2, second.id());
    }

    @Test
    void create_usesHighestIdPlusOne_afterDeleting() {
        int first = dao.create(person("Ana")).id();
        dao.create(person("Beto"));
        dao.delete(first);

        assertEquals(3, dao.create(person("Carla")).id());
    }

    @Test
    void create_rejectsCharactersNotAllowedInXml_andKeepsData() {
        dao.create(person("Ana"));

        assertThrows(IllegalArgumentException.class, () -> dao.create(person("a\u0001b")));

        assertEquals(1, dao.readAll().size());
    }

    @Test
    void save_keepsAccents_writesDeclaration_andLeavesNoTemporaryFile() throws IOException {
        dao.create(new Person(0, "María", "Peña", "", ""));

        assertEquals("María", dao.readAll().get(0).name());
        assertEquals("Peña", dao.readAll().get(0).lastName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", Files.readAllLines(xmlFile).get(0));
        assertFalse(Files.exists(tempDir.resolve("persons.xml.tmp")));
    }

    @Test
    void findById_returnsMatchingPerson() {
        dao.create(person("Ana"));
        int id = dao.create(person("Beto")).id();

        assertEquals("Beto", dao.findById(id).orElseThrow().name());
    }

    @Test
    void findById_isEmpty_whenNotFound() {
        dao.create(person("Ana"));

        assertTrue(dao.findById(99).isEmpty());
    }

    @Test
    void update_modifiesExistingPerson() {
        Person created = dao.create(person("Ana"));

        assertTrue(dao.update(new Person(created.id(), "Ana María", "Ruiz", "", "")));

        Person updated = dao.findById(created.id()).orElseThrow();
        assertEquals("Ana María", updated.name());
        assertEquals("Ruiz", updated.lastName());
    }

    @Test
    void update_returnsFalse_whenPersonNotFound() {
        assertFalse(dao.update(new Person(42, "Nadie", "", "", "")));
    }

    @Test
    void delete_removesPerson() {
        int first = dao.create(person("Ana")).id();
        int second = dao.create(person("Beto")).id();

        assertTrue(dao.delete(first));

        List<Person> all = dao.readAll();
        assertEquals(1, all.size());
        assertEquals(second, all.get(0).id());
    }

    @Test
    void delete_returnsFalse_whenPersonNotFound() {
        dao.create(person("Ana"));

        assertFalse(dao.delete(99));
        assertEquals(1, dao.readAll().size());
    }

    @Test
    void readAll_fails_whenFileIsCorrupt_andKeepsIt() throws IOException {
        String corrupt = "<persons><person>";
        Files.writeString(xmlFile, corrupt);

        assertThrows(IllegalStateException.class, () -> dao.readAll());

        assertEquals(corrupt, Files.readString(xmlFile));
    }

    @Test
    void readAll_fails_whenIdIsInvalid() throws IOException {
        Files.writeString(xmlFile, "<persons><person><id>abc</id></person></persons>");

        assertThrows(IllegalStateException.class, () -> dao.readAll());
    }

    @Test
    void readAll_fails_whenIdIsMissing() throws IOException {
        Files.writeString(xmlFile, "<persons><person><name>Ana</name></person></persons>");

        assertThrows(IllegalStateException.class, () -> dao.readAll());
    }

    @Test
    void readAll_rejectsExternalEntities() throws IOException {
        Path secret = tempDir.resolve("secret.txt");
        Files.writeString(secret, "contenido privado");
        Files.writeString(xmlFile, """
                <!DOCTYPE persons [<!ENTITY x SYSTEM "%s">]>
                <persons><person><id>1</id><name>&x;</name></person></persons>
                """.formatted(secret.toUri()));

        assertThrows(IllegalStateException.class, () -> dao.readAll());
    }
}
