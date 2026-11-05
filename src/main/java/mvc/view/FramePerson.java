package mvc.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class FramePerson extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNS = { "Id", "Nombre", "Apellido", "Email", "Teléfono" };

    private final JTextField txtId = new JTextField(5);
    private final JTextField txtName = new JTextField(20);
    private final JTextField txtLastName = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextField txtPhoneNumber = new JTextField(20);
    private final JButton btnSave = new JButton("Guardar");
    private final JButton btnUpdate = new JButton("Actualizar");
    private final JButton btnDelete = new JButton("Borrar");
    private final JButton btnList = new JButton("Listar");
    private final DefaultTableModel model = new DefaultTableModel(COLUMNS, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblList = new JTable(model);

    public FramePerson() {
        setTitle("Directorio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tblList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel container = new JPanel(new GridBagLayout());
        addField(container, "ID", txtId, 0);
        addField(container, "Nombre", txtName, 1);
        addField(container, "Apellido", txtLastName, 2);
        addField(container, "Email", txtEmail, 3);
        addField(container, "Teléfono", txtPhoneNumber, 4);

        JPanel buttons = new JPanel();
        buttons.add(btnSave);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnList);
        GridBagConstraints buttonsConstraints = constraints(0, 5);
        buttonsConstraints.gridwidth = GridBagConstraints.REMAINDER;
        container.add(buttons, buttonsConstraints);

        GridBagConstraints tableConstraints = constraints(0, 6);
        tableConstraints.gridwidth = GridBagConstraints.REMAINDER;
        tableConstraints.fill = GridBagConstraints.BOTH;
        tableConstraints.weightx = 1;
        tableConstraints.weighty = 1;
        JScrollPane scrollPane = new JScrollPane(tblList);
        scrollPane.setPreferredSize(new Dimension(750, 200));
        container.add(scrollPane, tableConstraints);

        setContentPane(container);
        // Si no cabe el tamaño preferido, GridBagLayout reduce los campos de texto casi a cero.
        pack();
        setMinimumSize(getSize());
        setSize(800, 500);
        setLocationRelativeTo(null);
    }

    private static void addField(JPanel container, String label, JTextField field, int row) {
        container.add(new JLabel(label), constraints(0, row));
        container.add(field, constraints(1, row));
    }

    private static GridBagConstraints constraints(int column, int row) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.NORTHWEST;
        return constraints;
    }

    public JTextField getTxtId() {
        return txtId;
    }

    public JTextField getTxtName() {
        return txtName;
    }

    public JTextField getTxtLastName() {
        return txtLastName;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JTextField getTxtPhoneNumber() {
        return txtPhoneNumber;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnList() {
        return btnList;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public JTable getTblList() {
        return tblList;
    }
}
