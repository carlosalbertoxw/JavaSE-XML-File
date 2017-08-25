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

	private static Document document;
	private static File XMLFile = new File(DAOPerson.class.getResource("persons.xml").getPath());
	private static Element root;

	public static void create(Person person) {
		loadFile();
		Element e = new Element("person");
		e.addContent(new Element("id").setText(String.valueOf(person.getId())));
		e.addContent(new Element("name").setText(person.getName()));
		e.addContent(new Element("lastName").setText(person.getLastName()));
		e.addContent(new Element("email").setText(person.getEmail()));
		e.addContent(new Element("phoneNumber").setText(person.getPhoneNumber()));
		root.addContent(e);
		saveChanges();
	}

	public static Person read(Person person) {
		loadFile();
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

	public static boolean delete(Person person) {
		loadFile();
		List<Element> elements = root.getChildren();
		for (Element element : elements) {
			if (element.getChild("id").getValue().equals(String.valueOf(person.getId()))) {
				elements.remove(element);
				saveChanges();
				return true;
			}
		}
		return false;
	}

	public static boolean update(Person person) {
		loadFile();
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
				saveChanges();
				return true;
			}
		}
		return false;
	}

	public static List<Person> readAll() {
		loadFile();
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

	public static void loadFile() {
		SAXBuilder builder = new SAXBuilder();

		try {
			document = builder.build(XMLFile);
			root = document.getRootElement();

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void saveChanges() {
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
