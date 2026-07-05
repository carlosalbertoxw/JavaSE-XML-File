package mvc.dao;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import mvc.model.Person;

public class DAOPerson {

    /** Nombre por defecto del archivo de datos, ubicado en el directorio de trabajo. */
    private static final String DEFAULT_FILE_NAME = "persons.xml";

    private final File xmlFile;

    public DAOPerson() {
        this(new File(DEFAULT_FILE_NAME));
    }

    /** Constructor con archivo explícito (útil para pruebas). */
    public DAOPerson(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public void create(Person person) {
        Element root = loadFile();
        root.addContent(toElement(person));
        saveChanges(root);
    }

    public Person read(Person person) {
        Element root = loadFile();
        for (Element element : root.getChildren()) {
            if (matchesId(element, person.getId())) {
                return fromElement(element);
            }
        }
        return null;
    }

    public boolean delete(Person person) {
        Element root = loadFile();
        List<Element> elements = root.getChildren();
        for (int i = 0; i < elements.size(); i++) {
            if (matchesId(elements.get(i), person.getId())) {
                elements.remove(i);
                saveChanges(root);
                return true;
            }
        }
        return false;
    }

    public boolean update(Person person) {
        Element root = loadFile();
        List<Element> elements = root.getChildren();
        for (int i = 0; i < elements.size(); i++) {
            if (matchesId(elements.get(i), person.getId())) {
                elements.set(i, toElement(person));
                saveChanges(root);
                return true;
            }
        }
        return false;
    }

    public List<Person> readAll() {
        Element root = loadFile();
        List<Person> persons = new ArrayList<>();
        for (Element element : root.getChildren()) {
            persons.add(fromElement(element));
        }
        return persons;
    }

    Element loadFile() {
        if (!xmlFile.exists() || xmlFile.length() == 0) {
            return new Element("persons");
        }
        try {
            Document document = new SAXBuilder().build(xmlFile);
            return document.getRootElement();
        } catch (JDOMException | IOException e) {
            throw new RuntimeException("No se pudo leer el archivo XML: " + xmlFile, e);
        }
    }

    private void saveChanges(Element root) {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        try (Writer writer = Files.newBufferedWriter(xmlFile.toPath(), StandardCharsets.UTF_8)) {
            xmlOutputter.output(root, writer);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar el archivo XML: " + xmlFile, e);
        }
    }

    private boolean matchesId(Element element, int id) {
        return element.getChildText("id") != null
                && element.getChildText("id").equals(String.valueOf(id));
    }

    private Element toElement(Person person) {
        Element e = new Element("person");
        e.addContent(new Element("id").setText(String.valueOf(person.getId())));
        e.addContent(new Element("name").setText(person.getName()));
        e.addContent(new Element("lastName").setText(person.getLastName()));
        e.addContent(new Element("email").setText(person.getEmail()));
        e.addContent(new Element("phoneNumber").setText(person.getPhoneNumber()));
        return e;
    }

    private Person fromElement(Element element) {
        Person person = new Person();
        person.setId(Integer.parseInt(element.getChildText("id")));
        person.setName(element.getChildText("name"));
        person.setLastName(element.getChildText("lastName"));
        person.setEmail(element.getChildText("email"));
        person.setPhoneNumber(element.getChildText("phoneNumber"));
        return person;
    }
}
