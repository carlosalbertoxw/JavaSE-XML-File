package mvc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import mvc.dao.DAOPerson;
import mvc.model.Person;
import mvc.view.FramePerson;

public class ControllerPerson implements ActionListener, MouseListener {

	private FramePerson framePerson;
	private Person person;
	private List<Person> persons;

	public ControllerPerson() {
		framePerson = new FramePerson();
		person = new Person();
		persons = new ArrayList<Person>();
		framePerson.getBtnSave().addActionListener(this);
		framePerson.getBtnSave().setActionCommand("SAVE");
		framePerson.getBtnUpdate().addActionListener(this);
		framePerson.getBtnUpdate().setActionCommand("UPDATE");
		framePerson.getBtnDelete().addActionListener(this);
		framePerson.getBtnDelete().setActionCommand("DELETE");
		framePerson.getBtnList().addActionListener(this);
		framePerson.getBtnList().setActionCommand("LIST");
		framePerson.getTblList().addMouseListener(this);
		framePerson.getTxtId().setEnabled(false);
		list();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("SAVE")) {
			save();
			list();
			cleanFields();
		} else if (e.getActionCommand().equals("UPDATE")) {
			update();
			list();
			cleanFields();
		} else if (e.getActionCommand().equals("DELETE")) {
			delete();
			list();
			cleanFields();
		} else if (e.getActionCommand().equals("LIST")) {
			list();
			cleanFields();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int fila = framePerson.getTblList().getSelectedRow();
		person.setId(Integer.parseInt(framePerson.getTblList().getValueAt(fila, 0).toString()));
		person = DAOPerson.read(person);
		framePerson.getTxtId().setText(String.valueOf(person.getId()));
		framePerson.getTxtName().setText(person.getName());
		framePerson.getTxtLastName().setText(person.getLastName());
		framePerson.getTxtEmail().setText(person.getEmail());
		framePerson.getTxtPhoneNumber().setText(person.getPhoneNumber());
	}

	private void save() {
		int id = 0;
		persons = DAOPerson.readAll();
		for (Person person : persons) {
			if (person.getId() > id) {
				id = person.getId();
			}
		}
		person.setId(id + 1);
		person.setName(framePerson.getTxtName().getText());
		person.setLastName(framePerson.getTxtLastName().getText());
		person.setEmail(framePerson.getTxtEmail().getText());
		person.setPhoneNumber(framePerson.getTxtPhoneNumber().getText());
		DAOPerson.create(person);
	}

	private void update() {
		if (!framePerson.getTxtId().getText().isEmpty()) {
			person.setId(Integer.parseInt(framePerson.getTxtId().getText()));
			person.setName(framePerson.getTxtName().getText());
			person.setLastName(framePerson.getTxtLastName().getText());
			person.setEmail(framePerson.getTxtEmail().getText());
			person.setPhoneNumber(framePerson.getTxtPhoneNumber().getText());
			DAOPerson.update(person);
		}
	}

	private void delete() {
		if (!framePerson.getTxtId().getText().isEmpty()) {
			person.setId(Integer.parseInt(framePerson.getTxtId().getText()));
			DAOPerson.delete(person);
		}
	}

	private void list() {
		persons = DAOPerson.readAll();
		framePerson.getModel().setRowCount(0);
		for (Person person : persons) {
			Object[] objects = new Object[6];
			objects[0] = person.getId();
			objects[1] = person.getName();
			objects[2] = person.getLastName();
			objects[3] = person.getEmail();
			objects[4] = person.getPhoneNumber();
			framePerson.getModel().addRow(objects);
		}
		framePerson.getTblList().setModel(framePerson.getModel());
	}

	private void cleanFields() {
		framePerson.getTxtId().setText("");
		framePerson.getTxtName().setText("");
		framePerson.getTxtLastName().setText("");
		framePerson.getTxtEmail().setText("");
		framePerson.getTxtPhoneNumber().setText("");
	}

	public static void main(String[] args) {
		new ControllerPerson();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
