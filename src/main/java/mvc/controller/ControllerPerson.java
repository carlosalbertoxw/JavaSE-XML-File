package mvc.controller;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import mvc.dao.DAOPerson;
import mvc.model.Person;
import mvc.view.FramePerson;

public class ControllerPerson {

    private final FramePerson view;
    private final DAOPerson dao;

    public ControllerPerson(FramePerson view, DAOPerson dao) {
        this.view = view;
        this.dao = dao;
        view.getTxtId().setEnabled(false);
        view.getBtnSave().addActionListener(e -> runSafely(this::save));
        view.getBtnUpdate().addActionListener(e -> runSafely(this::update));
        view.getBtnDelete().addActionListener(e -> runSafely(this::delete));
        view.getBtnList().addActionListener(e -> runSafely(this::refresh));
        view.getTblList().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                runSafely(this::showSelectedPerson);
            }
        });
    }

    private void save() {
        Person person = readForm(0);
        if (isValid(person)) {
            dao.create(person);
            refresh();
        }
    }

    private void update() {
        if (!hasPersonSelected()) {
            return;
        }
        Person person = readForm(selectedId());
        if (isValid(person)) {
            if (!dao.update(person)) {
                showError("La persona ya no existe.");
            }
            refresh();
        }
    }

    private void delete() {
        if (!hasPersonSelected()) {
            return;
        }
        if (!dao.delete(selectedId())) {
            showError("La persona ya no existe.");
        }
        refresh();
    }

    private void refresh() {
        List<Person> persons = dao.readAll();
        view.getModel().setRowCount(0);
        for (Person person : persons) {
            view.getModel().addRow(new Object[] {
                    person.id(), person.name(), person.lastName(), person.email(), person.phoneNumber() });
        }
        clearForm();
    }

    private void showSelectedPerson() {
        int row = view.getTblList().getSelectedRow();
        if (row >= 0) {
            dao.findById((int) view.getModel().getValueAt(row, 0)).ifPresent(this::fillForm);
        }
    }

    private boolean hasPersonSelected() {
        if (view.getTxtId().getText().isEmpty()) {
            showError("Selecciona una persona de la lista.");
            return false;
        }
        return true;
    }

    private int selectedId() {
        return Integer.parseInt(view.getTxtId().getText());
    }

    private boolean isValid(Person person) {
        person.findValidationError().ifPresent(this::showError);
        return person.findValidationError().isEmpty();
    }

    private Person readForm(int id) {
        return new Person(
                id,
                textOf(view.getTxtName()),
                textOf(view.getTxtLastName()),
                textOf(view.getTxtEmail()),
                textOf(view.getTxtPhoneNumber()));
    }

    private static String textOf(JTextField field) {
        return field.getText().strip();
    }

    private void fillForm(Person person) {
        view.getTxtId().setText(String.valueOf(person.id()));
        view.getTxtName().setText(person.name());
        view.getTxtLastName().setText(person.lastName());
        view.getTxtEmail().setText(person.email());
        view.getTxtPhoneNumber().setText(person.phoneNumber());
    }

    private void clearForm() {
        view.getTxtId().setText("");
        view.getTxtName().setText("");
        view.getTxtLastName().setText("");
        view.getTxtEmail().setText("");
        view.getTxtPhoneNumber().setText("");
    }

    /** Muestra los errores al usuario, porque la consola no existe al abrir el JAR con doble clic. */
    private void runSafely(Runnable action) {
        try {
            action.run();
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Directorio", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FramePerson view = new FramePerson();
            ControllerPerson controller = new ControllerPerson(view, new DAOPerson());
            try {
                controller.refresh();
                view.setVisible(true);
            } catch (RuntimeException e) {
                controller.showError(e.getMessage());
                System.exit(1);
            }
        });
    }
}
