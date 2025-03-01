/*
 * 
 * This is a dialog for adding new Employees and saving records to file
 * 
 * */

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class AddRecordDialog extends JDialog implements ActionListener {
	JTextField idField, ppsNumberField, surnameField, firstNameField, salaryField;
	JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	JButton save, cancel;
	EmployeeDetails parent;
	// constructor for add record dialog
	public AddRecordDialog(EmployeeDetails parent) {
		setTitle("Add Record");
		setModal(true);
		this.parent = parent;
		this.parent.setEnabled(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JScrollPane scrollPane = new JScrollPane(dialogPane());
		setContentPane(scrollPane);
		
		getRootPane().setDefaultButton(save);
		
		setSize(500, 370);
		setLocation(350, 250);
		setVisible(true);
	}// end AddRecordDialog

	// initialize dialog container
	public Container dialogPane() {
		JPanel employeeDetailsPanel, buttonPanel;
		employeeDetailsPanel = new JPanel(new MigLayout());
		buttonPanel = new JPanel();
		JTextField field;

		employeeDetailsPanel.setBorder(BorderFactory.createTitledBorder("Employee Details"));

		employeeDetailsPanel.add(new JLabel("ID:"), "growx, pushx");
		employeeDetailsPanel.add(idField = new JTextField(20), "growx, pushx, wrap");
		idField.setEditable(false);
		

		employeeDetailsPanel.add(new JLabel("PPS Number:"), "growx, pushx");
		employeeDetailsPanel.add(ppsNumberField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Surname:"), "growx, pushx");
		employeeDetailsPanel.add(surnameField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("First Name:"), "growx, pushx");
		employeeDetailsPanel.add(firstNameField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Gender:"), "growx, pushx");
		employeeDetailsPanel.add(genderCombo = new JComboBox<String>(this.parent.gender), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Department:"), "growx, pushx");
		employeeDetailsPanel.add(departmentCombo = new JComboBox<String>(this.parent.department), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Salary:"), "growx, pushx");
		employeeDetailsPanel.add(salaryField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Full Time:"), "growx, pushx");
		employeeDetailsPanel.add(fullTimeCombo = new JComboBox<String>(this.parent.fullTime), "growx, pushx, wrap");

		buttonPanel.add(save = new JButton("Save"));
		save.addActionListener(this);
		save.requestFocus();
		buttonPanel.add(cancel = new JButton("Cancel"));
		cancel.addActionListener(this);

		employeeDetailsPanel.add(buttonPanel, "span 2,growx, pushx,wrap");
		// loop through all panel components and add fonts and listeners
		for (int i = 0; i < employeeDetailsPanel.getComponentCount(); i++) {
			employeeDetailsPanel.getComponent(i).setFont(this.parent.font1);
			if (employeeDetailsPanel.getComponent(i) instanceof JComboBox) {
				employeeDetailsPanel.getComponent(i).setBackground(Color.WHITE);
			}// end if
			else if(employeeDetailsPanel.getComponent(i) instanceof JTextField){
				field = (JTextField) employeeDetailsPanel.getComponent(i);
				if(field == ppsNumberField)
					field.setDocument(new JTextFieldLimit(9));
				else
				field.setDocument(new JTextFieldLimit(20));
			}// end else if
		}// end for
		idField.setText(Integer.toString(this.parent.getNextFreeId()));
		return employeeDetailsPanel;
	}

	// add record to file
	public void addRecord() {
		boolean fullTime = false;
		Employee theEmployee;

		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;
		// create new Employee record with details from text fields
		theEmployee = new Employee(Integer.parseInt(idField.getText()), ppsNumberField.getText().trim().toUpperCase(), surnameField.getText().toUpperCase(),
				firstNameField.getText().toUpperCase(), genderCombo.getSelectedItem().toString().charAt(0),
				departmentCombo.getSelectedItem().toString(), Double.parseDouble(salaryField.getText()), fullTime);
		this.parent.currentEmployee = theEmployee;
		this.parent.addRecord(theEmployee);
		this.parent.displayRecords(theEmployee);
	}

	// check for input in text fields
	public boolean checkInput() {
		boolean valid = true;
		// Validate PPS
		if (!EmployeeInputValidator.isNotEmpty(ppsNumberField.getText())
				|| !EmployeeInputValidator.isValidPps(ppsNumberField.getText())) {
			ppsNumberField.setBackground(new Color(255, 150, 150));
			valid = false;
		}

		// Validate surname
		if (!EmployeeInputValidator.isNotEmpty(surnameField.getText())) {
			surnameField.setBackground(new Color(255, 150, 150));
			valid = false;
		}

		// Validate first name
		if (!EmployeeInputValidator.isNotEmpty(firstNameField.getText())) {
			firstNameField.setBackground(new Color(255, 150, 150));
			valid = false;
		}

		// Validate salary
		if (!EmployeeInputValidator.isValidSalary(salaryField.getText())) {
			salaryField.setBackground(new Color(255, 150, 150));
			valid = false;
		}

		// Check combos for e.g. 0 index => not selected
		if (genderCombo.getSelectedIndex() == 0) {
			genderCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		}
		if (departmentCombo.getSelectedIndex() == 0) {
			departmentCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		}
		if (fullTimeCombo.getSelectedIndex() == 0) {
			fullTimeCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		}

		if (!valid) {
			JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
		}
		return valid;
	}

	// set text field to white colour
	public void setToWhite() {
		ppsNumberField.setBackground(Color.WHITE);
		surnameField.setBackground(Color.WHITE);
		firstNameField.setBackground(Color.WHITE);
		salaryField.setBackground(Color.WHITE);
		genderCombo.setBackground(Color.WHITE);
		departmentCombo.setBackground(Color.WHITE);
		fullTimeCombo.setBackground(Color.WHITE);
	}// end setToWhite

	// action performed
	public void actionPerformed(ActionEvent e) {
		// if chosen option save, save record to file
		if (e.getSource() == save) {
			// if inputs correct, save record
			if (checkInput()) {
				addRecord();// add record to file
				dispose();// dispose dialog
				this.parent.changesMade = true;
			}// end if
			// else display message and set text fields to white colour
			else {
				JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
				setToWhite();
			}// end else
		}// end if
		else if (e.getSource() == cancel)
			dispose();// dispose dialog
	}// end actionPerformed
}// end class AddRecordDialog