package mvc.dao;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.IllegalDataException;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import mvc.model.Person;

public class DAOPerson {

    private static final Path DEFAULT_FILE = Path.of("persons.xml");

    /** Rechazar DOCTYPE impide que un XML malicioso lea archivos mediante entidades externas (XXE). */
    private static final String DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";

    private final Path xmlFile;

    public DAOPerson() {
        this(DEFAULT_FILE);
    }

    public DAOPerson(Path xmlFile) {
        this.xmlFile = xmlFile;
    }

    /** Guarda la persona con el siguiente ID disponible y la devuelve con ese ID. */
    public Person create(Person person) {
        Document document = load();
        Element root = document.getRootElement();
        int nextId = root.getChildren().stream().mapToInt(DAOPerson::idOf).max().orElse(0) + 1;
        Person created = new Person(nextId, person.name(), person.lastName(), person.email(), person.phoneNumber());
        root.addContent(toElement(created));
        save(document);
        return created;
    }

    public Optional<Person> findById(int id) {
        return readAll().stream().filter(person -> person.id() == id).findFirst();
    }

    public List<Person> readAll() {
        return load().getRootElement().getChildren().stream().map(DAOPerson::fromElement).toList();
    }

    public boolean update(Person person) {
        Document document = load();
        List<Element> elements = document.getRootElement().getChildren();
        for (int i = 0; i < elements.size(); i++) {
            if (idOf(elements.get(i)) == person.id()) {
                elements.set(i, toElement(person));
                save(document);
                return true;
            }
        }
        return false;
    }

    public boolean delete(int id) {
        Document document = load();
        boolean removed = document.getRootElement().getChildren().removeIf(element -> idOf(element) == id);
        if (removed) {
            save(document);
        }
        return removed;
    }

    private Document load() {
        try {
            if (Files.notExists(xmlFile) || Files.size(xmlFile) == 0) {
                return new Document(new Element("persons"));
            }
            SAXBuilder builder = new SAXBuilder();
            builder.setFeature(DISALLOW_DOCTYPE, true);
            return builder.build(xmlFile.toFile());
        } catch (JDOMException | IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo XML: " + xmlFile, e);
        }
    }

    /** Escribe en un archivo temporal y lo mueve al final, para no truncar los datos si la escritura falla. */
    private void save(Document document) {
        Path temp = xmlFile.resolveSibling(xmlFile.getFileName() + ".tmp");
        try {
            try (Writer writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
                new XMLOutputter(Format.getPrettyFormat()).output(document, writer);
            }
            Files.move(temp, xmlFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo XML: " + xmlFile, e);
        }
    }

    private static int idOf(Element element) {
        String id = element.getChildText("id");
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("El archivo XML contiene un ID no válido: " + id, e);
        }
    }

    private static Element toElement(Person person) {
        try {
            return new Element("person")
                    .addContent(new Element("id").setText(String.valueOf(person.id())))
                    .addContent(new Element("name").setText(person.name()))
                    .addContent(new Element("lastName").setText(person.lastName()))
                    .addContent(new Element("email").setText(person.email()))
                    .addContent(new Element("phoneNumber").setText(person.phoneNumber()));
        } catch (IllegalDataException e) {
            throw new IllegalArgumentException("Los datos contienen caracteres que no se pueden guardar en XML.", e);
        }
    }

    private static Person fromElement(Element element) {
        return new Person(
                idOf(element),
                element.getChildText("name"),
                element.getChildText("lastName"),
                element.getChildText("email"),
                element.getChildText("phoneNumber"));
    }
}
