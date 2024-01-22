package com.patient.app;

import javax.swing.*;

import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import com.patient.data.DatabaseManager;
import com.patient.data.PatientTableModel;
import com.patient.model.Patient;
import com.patient.renderer.TableRenderer;
import com.patient.repository.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EventObject;
import java.util.List;

public class PatientWindow extends JFrame {

	private PatientRepositoryImpl repository = new PatientRepositoryImpl();

	private PatientTableModel patientModel;
	private JTextField idField;
	private JTextField firstNameField;
	private JTextField lastNameField;
	private JComboBox genderComboBox;
	private JCheckBox insuranceCheckBox;
	private JTextArea addressField;

	private DatabaseManager dbm;

	private JTable tableModel = new JTable(patientModel);

	public PatientWindow() {
		super("Patient Information");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLocationRelativeTo(null);
		setBackground(Color.white);

		// initialize patient model
		patientModel = new PatientTableModel();

		// initialize all the panels
		JPanel inputPanel = createInputPanel();
		tableModel = createTable();
		JScrollPane tableScrollPane = new JScrollPane(tableModel);
		tableScrollPane.setBorder(BorderFactory.createTitledBorder("Patient Information "));
		JPanel btnPanel = submitButtonPanel();

		// Continuously listen to the cell data and update the database on change
		patientModel.addTableModelListener(new TableModelListener() {
//			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
				int column = e.getColumn();

				// Only update the database if the change occurred in a valid cell
				if (row != TableModelEvent.HEADER_ROW && column != TableModelEvent.ALL_COLUMNS) {
					updatePatientInDatabase(row, column);
				}
			}
		});

		// --------------- DELETE BUTTON --------------------------------------

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelectedPatients();
			}
		});

		// ---------------- REFRESH BUTTON -----------------------

		JButton refreshButton = new JButton("Refresh");
		refreshButton.setBackground(Color.lightGray);
		refreshButton.addActionListener(new ActionListener() {

//			@Override
			public void actionPerformed(ActionEvent e) {
				patientModel.clearPatitents();
				createTable();
			}

		});

		deleteButton.setBackground(Color.RED);
		inputPanel.setBackground(Color.WHITE);
		tableScrollPane.setBackground(Color.white);

		btnPanel.add(refreshButton);
		btnPanel.add(deleteButton);
		add(inputPanel);
		add(btnPanel);
		add(tableScrollPane);
		setVisible(true);

		firstNameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isLetter(e.getKeyChar())) {
					e.consume();
				}
			}
		});
	}

	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
		inputPanel.setBorder(BorderFactory.createTitledBorder("Enter the Patient data"));

		idField = new JTextField();
		firstNameField = new JTextField();
		lastNameField = new JTextField();
		String[] genderOptions = { "Male", "Female", "Other" };
		genderComboBox = new JComboBox<>(genderOptions);
		insuranceCheckBox = new JCheckBox();
		addressField = new JTextArea();

		insuranceCheckBox.setBackground(Color.WHITE);
		addressField.setBorder(BorderFactory.createLineBorder(Color.lightGray));

// submit button code....

		inputPanel.add(new JLabel("ID:"));
		inputPanel.add(idField);
		inputPanel.add(new JLabel("First Name:"));
		inputPanel.add(firstNameField);
		inputPanel.add(new JLabel("Last Name:"));
		inputPanel.add(lastNameField);
		inputPanel.add(new JLabel("Gender:"));
		inputPanel.add(genderComboBox);
		inputPanel.add(new JLabel("Address:"));
		inputPanel.add(addressField);
		inputPanel.add(new JLabel("Has Insurance:"));
		inputPanel.add(insuranceCheckBox);

//		firstNameField.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				if(!Character.isLetter(e.getKeyChar())) {e.consume();}
//			}
//		});

		return inputPanel;
	}

	// ----------- SUBMIT BUTTON PANEL -------------------------------------------
	private JPanel submitButtonPanel() {

		JPanel submitButtonPanel = new JPanel();
		JButton submitButton = new JButton("Submit");

		submitButton.addActionListener(new ActionListener() {

//			@Override
			public void actionPerformed(ActionEvent e) {
				addPatient();
			}
		});

		submitButtonPanel.setBackground(Color.white);
		submitButton.setBackground(Color.GREEN);

		submitButtonPanel.add(submitButton);
		return submitButtonPanel;
	}

	private void clearInputFields(JTextField idField, JTextField firstNameField, JTextField lastNameField,
			JComboBox<String> genderComboBox, JCheckBox insuranceCheckBox, JTextArea addressField) {
		idField.setText("");
		firstNameField.setText("");
		lastNameField.setText("");
		genderComboBox.setSelectedIndex(0);
		insuranceCheckBox.setSelected(false);
		addressField.setText("");
	}

	private JTable createTable() {
		try {

			List<Patient> patientList = repository.getAllPatient();
			for (Patient patient : patientList) {
				patientModel.addPatient(patient);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error while loading data", "Warning", JOptionPane.ERROR_MESSAGE);
		}

		JTable table = new JTable(patientModel);

		// set cell editor to allow editing in the table
		table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));

		table.setDefaultRenderer(Object.class, new TableRenderer());
		// table.setDefaultRenderer(Object.class, new InsuranceCellRenderer());

		TableColumn genderColumn = table.getColumnModel().getColumn(3);

		String[] genderOptions = { "Male", "Female", "Other" };
		JComboBox<String> genderCellComboBox = new JComboBox<String>(genderOptions);
		genderColumn.setCellEditor(new DefaultCellEditor(genderCellComboBox));

		TableColumn hasInsuranceColumn = table.getColumnModel().getColumn(5);

		String[] insuranceOptions = { "Yes", "No" };
		JComboBox<String> insuranceCellComboBox = new JComboBox<String>(insuranceOptions);
		hasInsuranceColumn.setCellEditor(new DefaultCellEditor(insuranceCellComboBox));

//		TableColumn hasInsuranceColumn = table.getColumnModel().getColumn(5);
//		JCheckBox insuranceCellCheckBox = new JCheckBox();
//		hasInsuranceColumn.setCellEditor(new DefaultCellEditor(insuranceCellCheckBox));

		return table;
	}

	// ----------- ADD PATIENT TO LOCAL TABLE ---------------b

	public void addPatient() {
		if (areAllFieldsFilled()) {
			String id = idField.getText();
			String firstName = firstNameField.getText();
			String lastName = lastNameField.getText();
			String gender = (String) genderComboBox.getSelectedItem();
			boolean hasInsurance = insuranceCheckBox.isSelected();
			String address = addressField.getText();

			String hasInsuranceString;
			if (hasInsurance == true) {
				hasInsuranceString = "Yes";
			} else {
				hasInsuranceString = "No";
			}

			Patient patient = new Patient(id, firstName, lastName, gender, address, hasInsuranceString);
			patientModel.addPatient(patient);
			// addPatientToDatabase(patient);
			repository.addPatientToDB(patient);

			clearInputFields(idField, firstNameField, lastNameField, genderComboBox, insuranceCheckBox, addressField);
		} else {
			// Display warning in UI
			JOptionPane.showMessageDialog(PatientWindow.this, "Please enter data in all fields.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private boolean areAllFieldsFilled() {
		return !idField.getText().isEmpty() && !firstNameField.getText().isEmpty() && !lastNameField.getText().isEmpty()
				&& genderComboBox.getSelectedItem() != null && !addressField.getText().isEmpty();
	}


	// ------------ UPDATE DATABASE ON DATA CHANGE --------------------

	private void updatePatientInDatabase(int row, int column) {
		String id = (String) patientModel.getValueAt(row, 0); // Assuming id is in the first column
		String columnName = patientModel.getColumnName(column);
		Object updatedValue = patientModel.getValueAt(row, column);
		try {
			repository.updatePatient(id, columnName, updatedValue);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error while updating data", "Warning", JOptionPane.ERROR_MESSAGE);
        }

	}

	// -------------- DELETE OPERATION ----------------------------

	private void deleteSelectedPatients() {
		int[] selectedRows = tableModel.getSelectedRows();

		if (selectedRows.length > 0) {
			int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected records?",
					"Confirmation", JOptionPane.YES_NO_OPTION);

			if (option == JOptionPane.YES_OPTION) {
				for (int i = selectedRows.length - 1; i >= 0; i--) {
					int rowIndex = selectedRows[i];
					String patientId = (String) patientModel.getValueAt(rowIndex, 0);
					// deletePatientFromDatabase(selectedRows[i]);

					try {
						repository.deletePatient(patientId);

					} catch (Exception e) {
						System.out.println("Error in the deleteSelctedPateints() method.. ");
						e.printStackTrace();
						JOptionPane.showMessageDialog(this, "Error while deleting data", "Warning",
								JOptionPane.ERROR_MESSAGE);
					}

					patientModel.removePatient(rowIndex);
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "Please select at least one record to delete.", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

//	private void deletePatientFromDatabase(int rowIndex) {
//
//		String patientId = (String) patientModel.getValueAt(rowIndex, 0);
//
//		try {
//		} catch (SQLException e) {
//			System.out.println("Error in the method !");
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(this, "Error while deleting data", "Warning", JOptionPane.ERROR_MESSAGE);
//		}
//
//		patientModel.removePatient(rowIndex);
//	}

}