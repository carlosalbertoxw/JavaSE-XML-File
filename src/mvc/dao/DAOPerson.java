package mvc.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import mvc.model.Person;

public class DAOPerson {

    private File XMLFile = new File(DAOPerson.class.getResource("persons.xml").getPath());

    public void create(Person person) {
        Element root = loadFile();
        Element e = new Element("person");
        e.addContent(new Element("id").setText(String.valueOf(person.getId())));
        e.addContent(new Element("name").setText(person.getName()));
        e.addContent(new Element("lastName").setText(person.getLastName()));
        e.addContent(new Element("email").setText(person.getEmail()));
        e.addContent(new Element("phoneNumber").setText(person.getPhoneNumber()));
        root.addContent(e);
        saveChanges(root);
    }

    public Person read(Person person) {
        Element root = loadFile();
        List<Element> elements = root.getChildren();
        for (Element element : elements) {
            if (element.getChild("id").getValue().equals(String.valueOf(person.getId()))) {
                person.setId(Integer.parseInt(element.getChildText("id")));
                person.setName(element.getChildText("name"));
                person.setLastName(element.getChildText("lastName"));
                person.setEmail(element.getChildText("email"));
                person.setPhoneNumber(element.getChildText("phoneNumber"));
            }
        }
        return person;
    }

    public boolean delete(Person person) {
        Element root = loadFile();
        List<Element> elements = root.getChildren();
        for (Element element : elements) {
            if (element.getChild("id").getValue().equals(String.valueOf(person.getId()))) {
                elements.remove(element);
                saveChanges(root);
                return true;
            }
        }
        return false;
    }

    public boolean update(Person person) {
        Element root = loadFile();
        List<Element> elements = root.getChildren();
        for (Element element : elements) {
            if (element.getChild("id").getValue().equals(String.valueOf(person.getId()))) {
                Element e = new Element("person");
                e.addContent(new Element("id").setText(String.valueOf(person.getId())));
                e.addContent(new Element("name").setText(person.getName()));
                e.addContent(new Element("lastName").setText(person.getLastName()));
                e.addContent(new Element("email").setText(person.getEmail()));
                e.addContent(new Element("phoneNumber").setText(person.getPhoneNumber()));
                elements.set(elements.indexOf(element), e);
                saveChanges(root);
                return true;
            }
        }
        return false;
    }

    public List<Person> readAll() {
        Element root = loadFile();
        List<Person> persons = new ArrayList<Person>();
        List<Element> elements = root.getChildren();
        for (Element element : elements) {
            Person person = new Person();
            person.setId(Integer.parseInt(element.getChildText("id")));
            person.setName(element.getChildText("name"));
            person.setLastName(element.getChildText("lastName"));
            person.setEmail(element.getChildText("email"));
            person.setPhoneNumber(element.getChildText("phoneNumber"));
            persons.add(person);
        }
        return persons;
    }

    public Element loadFile() {
        SAXBuilder builder = new SAXBuilder();
        Element root = null;
        try {
            Document document = builder.build(XMLFile);
            root = document.getRootElement();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    private void saveChanges(Element root) {
        XMLOutputter xmlOutputter = new XMLOutputter();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(XMLFile);
            xmlOutputter.output(root, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
