package mvc.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.table.DefaultTableModel;

public class FramePerson extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel container;
	private JLabel lblId;
	private JTextField txtId;
	private JLabel lblName;
	private JTextField txtName;
	private JLabel lblLastName;
	private JTextField txtLastName;
	private JLabel lblEmail;
	private JTextField txtEmail;
	private JLabel lblPhoneNumber;
	private JTextField txtPhoneNumber;
	private JPanel ButtonsPanel;
	private JButton btnSave;
	private JButton btnDelete;
	private JButton btnUpdate;
	private JButton btnList;
	private JTable tblList;
	private DefaultTableModel model;
	private JScrollPane scrollPane;

	public FramePerson() {
		this.setSize(800, 500);
		this.setTitle("Directorio");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		container = new JPanel();
		this.setContentPane(container);
		container.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.anchor = GridBagConstraints.NORTHWEST;

		lblId = new JLabel("ID");
		constraints.gridx = 0;
		constraints.gridy = 0;
		container.add(lblId, constraints);

		txtId = new JTextField(5);
		constraints.gridx = 1;
		container.add(txtId, constraints);

		lblName = new JLabel("Nombre");
		constraints.gridx = 0;
		constraints.gridy = 1;
		container.add(lblName, constraints);

		txtName = new JTextField(20);
		constraints.gridx = 1;
		container.add(txtName, constraints);

		lblLastName = new JLabel("Apellido");
		constraints.gridx = 0;
		constraints.gridy = 2;
		container.add(lblLastName, constraints);

		txtLastName = new JTextField(20);
		constraints.gridx = 1;
		container.add(txtLastName, constraints);

		lblEmail = new JLabel("Email");
		constraints.gridx = 0;
		constraints.gridy = 3;
		container.add(lblEmail, constraints);

		txtEmail = new JTextField(20);
		constraints.gridx = 1;
		container.add(txtEmail, constraints);

		lblPhoneNumber = new JLabel("Teléfono");
		constraints.gridx = 0;
		constraints.gridy = 4;
		container.add(lblPhoneNumber, constraints);

		txtPhoneNumber = new JTextField(20);
		constraints.gridx = 1;
		container.add(txtPhoneNumber, constraints);

		ButtonsPanel = new JPanel(new FlowLayout());
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		container.add(ButtonsPanel, constraints);

		btnSave = new JButton("Guardar");
		ButtonsPanel.add(btnSave);

		btnUpdate = new JButton("Actualizar");
		ButtonsPanel.add(btnUpdate);

		btnDelete = new JButton("Borrar");
		ButtonsPanel.add(btnDelete);

		btnList = new JButton("Listar");
		ButtonsPanel.add(btnList);

		tblList = new JTable();
		tblList.setEditingColumn(0);
		String[] columnsName = new String[] { "Id", "Nombre", "Apellido", "Email", "Teléfono" };
		model = new DefaultTableModel(null, columnsName) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};
		model.setRowCount(0);
		tblList.setModel(model);

		scrollPane = new JScrollPane(tblList);
		scrollPane.setPreferredSize(new Dimension(750, 200));
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		container.add(scrollPane, constraints);

		this.setVisible(true);
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
