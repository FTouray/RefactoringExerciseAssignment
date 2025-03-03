
/* * 
 * This is a menu driven system that will allow users to define a data structure representing a collection of 
 * records that can be displayed both by means of a dialog that can be scrolled through and by means of a table
 * to give an overall view of the collection contents.
 * 
 * */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

public class EmployeeDetails extends JFrame implements ActionListener, ItemListener, DocumentListener, WindowListener, EmployeeModelObserver {
	// decimal format for inactive currency text field
	private static final DecimalFormat format = new DecimalFormat("\u20ac ###,###,##0.00");
	// decimal format for active currency text field
	private static final DecimalFormat fieldFormat = new DecimalFormat("0.00");
	// hold object start position in file
	private long currentByteStart = 0;
	// private RandomFile application = RandomFile.getInstance();
	private EmployeeDataModel model;
	// display files in File Chooser only with extension .dat
	private FileNameExtensionFilter datfilter = new FileNameExtensionFilter("dat files (*.dat)", "dat");
	// hold file name and path for current file in use
	private File file;
	// holds true or false if any changes are made for text fields
	private boolean change = false;
	// holds true or false if any changes are made for file content
	boolean changesMade = false;
	private JMenuItem open, save, saveAs, create, modify, delete, firstItem, lastItem, nextItem, prevItem, searchById,
			searchBySurname, listAll, closeApp;
	private JButton first, previous, next, last, add, edit, deleteButton, displayAll, searchId, searchSurname,
			saveChange, cancelChange;
	private JComboBox<String> genderCombo, departmentCombo, fullTimeCombo;
	private JTextField idField, ppsNumberField, surnameField, firstNameField, salaryField;
	private static EmployeeDetails frame = new EmployeeDetails();
	// font for labels, text fields and combo boxes
	Font font1 = new Font("SansSerif", Font.BOLD, 16);
	// holds automatically generated file name
	String generatedFileName;
	// holds current Employee object
	Employee currentEmployee;
	JTextField searchByIdField, searchBySurnameField;
	// gender combo box values
	String[] gender = { "", "M", "F" };
	// department combo box values
	String[] department = { "", "Administration", "Production", "Transport", "Management" };
	// full time combo box values
	String[] fullTime = { "", "Yes", "No" };

	// initialize menu bar
	private JMenuBar menuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu, recordMenu, navigateMenu, closeMenu;

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		recordMenu = new JMenu("Records");
		recordMenu.setMnemonic(KeyEvent.VK_R);
		navigateMenu = new JMenu("Navigate");
		navigateMenu.setMnemonic(KeyEvent.VK_N);
		closeMenu = new JMenu("Exit");
		closeMenu.setMnemonic(KeyEvent.VK_E);

		menuBar.add(fileMenu);
		menuBar.add(recordMenu);
		menuBar.add(navigateMenu);
		menuBar.add(closeMenu);

		fileMenu.add(open = new JMenuItem("Open")).addActionListener(this);
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(save = new JMenuItem("Save")).addActionListener(this);
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(saveAs = new JMenuItem("Save As")).addActionListener(this);
		saveAs.setMnemonic(KeyEvent.VK_F2);
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));

		recordMenu.add(create = new JMenuItem("Create new Record")).addActionListener(this);
		create.setMnemonic(KeyEvent.VK_N);
		create.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		recordMenu.add(modify = new JMenuItem("Modify Record")).addActionListener(this);
		modify.setMnemonic(KeyEvent.VK_E);
		modify.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		recordMenu.add(delete = new JMenuItem("Delete Record")).addActionListener(this);

		navigateMenu.add(firstItem = new JMenuItem("First"));
		firstItem.addActionListener(this);
		navigateMenu.add(prevItem = new JMenuItem("Previous"));
		prevItem.addActionListener(this);
		navigateMenu.add(nextItem = new JMenuItem("Next"));
		nextItem.addActionListener(this);
		navigateMenu.add(lastItem = new JMenuItem("Last"));
		lastItem.addActionListener(this);
		navigateMenu.addSeparator();
		navigateMenu.add(searchById = new JMenuItem("Search by ID")).addActionListener(this);
		navigateMenu.add(searchBySurname = new JMenuItem("Search by Surname")).addActionListener(this);
		navigateMenu.add(listAll = new JMenuItem("List all Records")).addActionListener(this);

		closeMenu.add(closeApp = new JMenuItem("Close")).addActionListener(this);
		closeApp.setMnemonic(KeyEvent.VK_F4);
		closeApp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));

		return menuBar;
	}// end menuBar

	// initialize search panel
	private JPanel searchPanel() {
		JPanel searchPanel = new JPanel(new MigLayout());

		searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
		searchPanel.add(new JLabel("Search by ID:"), "growx, pushx");
		searchPanel.add(searchByIdField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchByIdField.addActionListener(this);
		searchByIdField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(searchId = new JButton("Go"),
				"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchId.addActionListener(this);
		searchId.setToolTipText("Search Employee By ID");

		searchPanel.add(new JLabel("Search by Surname:"), "growx, pushx");
		searchPanel.add(searchBySurnameField = new JTextField(20), "width 200:200:200, growx, pushx");
		searchBySurnameField.addActionListener(this);
		searchBySurnameField.setDocument(new JTextFieldLimit(20));
		searchPanel.add(
				searchSurname = new JButton("Go"),"width 35:35:35, height 20:20:20, growx, pushx, wrap");
		searchSurname.addActionListener(this);
		searchSurname.setToolTipText("Search Employee By Surname");

		return searchPanel;
	}// end searchPanel

	// initialize navigation panel
	private JPanel navigPanel() {
		JPanel navigPanel = new JPanel();

		navigPanel.setBorder(BorderFactory.createTitledBorder("Navigate"));
		navigPanel.add(first = new JButton(new ImageIcon(
				new ImageIcon("first.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		first.setPreferredSize(new Dimension(17, 17));
		first.addActionListener(this);
		first.setToolTipText("Display first Record");

		navigPanel.add(previous = new JButton(new ImageIcon(new ImageIcon("prev.png").getImage()
				.getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		previous.setPreferredSize(new Dimension(17, 17));
		previous.addActionListener(this);
		previous.setToolTipText("Display next Record");

		navigPanel.add(next = new JButton(new ImageIcon(
				new ImageIcon("next.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		next.setPreferredSize(new Dimension(17, 17));
		next.addActionListener(this);
		next.setToolTipText("Display previous Record");

		navigPanel.add(last = new JButton(new ImageIcon(
				new ImageIcon("last.png").getImage().getScaledInstance(17, 17, java.awt.Image.SCALE_SMOOTH))));
		last.setPreferredSize(new Dimension(17, 17));
		last.addActionListener(this);
		last.setToolTipText("Display last Record");

		return navigPanel;
	}// end naviPanel

	private JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel();

		buttonPanel.add(add = new JButton("Add Record"), "growx, pushx");
		add.addActionListener(this);
		add.setToolTipText("Add new Employee Record");
		buttonPanel.add(edit = new JButton("Edit Record"), "growx, pushx");
		edit.addActionListener(this);
		edit.setToolTipText("Edit current Employee");
		buttonPanel.add(deleteButton = new JButton("Delete Record"), "growx, pushx, wrap");
		deleteButton.addActionListener(this);
		deleteButton.setToolTipText("Delete current Employee");
		buttonPanel.add(displayAll = new JButton("List all Records"), "growx, pushx");
		displayAll.addActionListener(this);
		displayAll.setToolTipText("List all Registered Employees");

		return buttonPanel;
	}

	// initialize main/details panel
	private JPanel detailsPanel() {
		JPanel employeeDetailsPanel = new JPanel(new MigLayout());
		JPanel buttonPanel = new JPanel();
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
		employeeDetailsPanel.add(genderCombo = new JComboBox<String>(gender), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Department:"), "growx, pushx");
		employeeDetailsPanel.add(departmentCombo = new JComboBox<String>(department), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Salary:"), "growx, pushx");
		employeeDetailsPanel.add(salaryField = new JTextField(20), "growx, pushx, wrap");

		employeeDetailsPanel.add(new JLabel("Full Time:"), "growx, pushx");
		employeeDetailsPanel.add(fullTimeCombo = new JComboBox<String>(fullTime), "growx, pushx, wrap");

		buttonPanel.add(saveChange = new JButton("Save"));
		saveChange.addActionListener(this);
		saveChange.setVisible(false);
		saveChange.setToolTipText("Save changes");
		buttonPanel.add(cancelChange = new JButton("Cancel"));
		cancelChange.addActionListener(this);
		cancelChange.setVisible(false);
		cancelChange.setToolTipText("Cancel edit");

		employeeDetailsPanel.add(buttonPanel, "span 2,growx, pushx,wrap");

		// loop through panel components and add listeners and format
		for (int i = 0; i < employeeDetailsPanel.getComponentCount(); i++) {
			employeeDetailsPanel.getComponent(i).setFont(font1);
			if (employeeDetailsPanel.getComponent(i) instanceof JTextField) {
				field = (JTextField) employeeDetailsPanel.getComponent(i);
				field.setEditable(false);
				if (field == ppsNumberField)
					field.setDocument(new JTextFieldLimit(9));
				else
					field.setDocument(new JTextFieldLimit(20));
				field.getDocument().addDocumentListener(this);
			} // end if
			else if (employeeDetailsPanel.getComponent(i) instanceof JComboBox) {
				employeeDetailsPanel.getComponent(i).setBackground(Color.WHITE);
				employeeDetailsPanel.getComponent(i).setEnabled(false);
				((JComboBox<String>) employeeDetailsPanel.getComponent(i)).addItemListener(this);
				((JComboBox<String>) employeeDetailsPanel.getComponent(i)).setRenderer(new DefaultListCellRenderer() {
					// set foregroung to combo boxes
					public void paint(Graphics g) {
						setForeground(new Color(65, 65, 65));
						super.paint(g);
					}// end paint
				});
			} // end else if
		} // end for
		return employeeDetailsPanel;
	}// end detailsPanel

	public EmployeeDetails() {
		super("Employee Details");
		model = new EmployeeDataModel();
		model.registerObserver(this);

		file = new File("Employee.dat"); // Default file
		model.loadAllFromFile(file.getAbsolutePath());
	}

	@Override
	public void onEmployeeListChanged() {
		// Refresh UI whenever data changes
		if (currentEmployee != null) {
			displayRecords(currentEmployee);
		}
	}

	// display current Employee details
	public void displayRecords(Employee thisEmployee) {
		if (thisEmployee == null || thisEmployee.getEmployeeId() == 0) {
			idField.setText("");
			ppsNumberField.setText("");
			surnameField.setText("");
			firstNameField.setText("");
			salaryField.setText("");
			genderCombo.setSelectedIndex(0);
			departmentCombo.setSelectedIndex(0);
			fullTimeCombo.setSelectedIndex(0);
		} else {
			idField.setText(String.valueOf(thisEmployee.getEmployeeId()));
			ppsNumberField.setText(thisEmployee.getPpsNumber().trim());
			surnameField.setText(thisEmployee.getSurname().trim());
			firstNameField.setText(thisEmployee.getFirstName());
			genderCombo.setSelectedItem(String.valueOf(thisEmployee.getGender()));
			departmentCombo.setSelectedItem(thisEmployee.getDepartment());
			salaryField.setText(format.format(thisEmployee.getSalary()));
			fullTimeCombo.setSelectedItem(thisEmployee.getFullTime() ? "Yes" : "No");
		}
		change = false;
	}

	private void firstRecord() {
    List<Employee> employees = model.getAll(); // Get all employees from the model

    if (!employees.isEmpty()) { 
        currentEmployee = employees.get(0); // Get the first employee
        displayRecords(currentEmployee); // Display the employee details
    } else {
        JOptionPane.showMessageDialog(this, "No Employees available!");
    }
	}

	// find byte start in file for previous active record
	private void previousRecord() {
		int index = model.getAll().indexOf(currentEmployee);
		if (index > 0) {
			currentEmployee = model.getAll().get(index - 1);
			displayRecords(currentEmployee);
		}
	}// end previousRecord

	// find byte start in file for next active record
	private void nextRecord() {
		int index = model.getAll().indexOf(currentEmployee);
		if (index < model.getAll().size() - 1) {
			currentEmployee = model.getAll().get(index + 1);
			displayRecords(currentEmployee);
		}
	}// end nextRecord

	// find byte start in file for last active record
	private void lastRecord() {
		if (!model.getAll().isEmpty()) {
			currentEmployee = model.getAll().get(model.getAll().size() - 1);
			displayRecords(currentEmployee);
		}
	}// end lastRecord

	private Employee findEmployee(SearchStrategy strategy, String searchValue) {
		return model.getAll().stream()
				.filter(e -> strategy.matches(e, searchValue))
				.findFirst()
				.orElse(null);
	}

	// search Employee by ID
	public void searchEmployeeById() {
		try {
			String input = searchByIdField.getText().trim();
			Employee found = findEmployee(new IdSearchStrategy(), input);

			if (found != null) {
				displayRecords(found);
			} else {
				JOptionPane.showMessageDialog(null, "Employee not found!");
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Invalid ID format!");
		}
	}

	// search Employee by surname
	public void searchEmployeeBySurname() {
		String input = searchBySurnameField.getText().trim();
		Employee found = findEmployee(new SurnameSearchStrategy(), input);

		if (found != null) {
			displayRecords(found);
		} else {
			JOptionPane.showMessageDialog(null, "Employee not found!");
		}
	}

	// get next free ID from Employees in the file
	public int getNextFreeId() {
		int nextFreeId = 0;
		// if file is empty or all records are empty start with ID 1 else look
		// for last active record
		if (file.length() == 0 || !isSomeoneToDisplay())
			nextFreeId++;
		else {
			lastRecord();// look for last active record
			// add 1 to last active records ID to get next ID
			nextFreeId = currentEmployee.getEmployeeId() + 1;
		}
		return nextFreeId;
	}// end getNextFreeId

	// get values from text fields and create Employee object
	private Employee getChangedDetails() {
		boolean fullTime = false;
		Employee theEmployee;
		if (((String) fullTimeCombo.getSelectedItem()).equalsIgnoreCase("Yes"))
			fullTime = true;

		theEmployee = new Employee(Integer.parseInt(idField.getText()), ppsNumberField.getText().toUpperCase(),
				surnameField.getText().toUpperCase(), firstNameField.getText().toUpperCase(),
				genderCombo.getSelectedItem().toString().charAt(0), departmentCombo.getSelectedItem().toString(),
				Double.parseDouble(salaryField.getText()), fullTime);

		return theEmployee;
	}// end getChangedDetails

	// add Employee object to fail
	public void addRecord(Employee newEmployee) {
		model.addEmployee(newEmployee, file.getAbsolutePath());
	}

	// delete (make inactive - empty) record from file
	private void deleteRecord() {
		if (isSomeoneToDisplay()) {
			int returnVal = JOptionPane.showOptionDialog(
					frame, "Do you want to delete this record?", "Delete",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

			if (returnVal == JOptionPane.YES_OPTION) {
				model.removeEmployee(currentEmployee, file.getAbsolutePath());
			}
		}
	}

	// create vector of vectors with all Employee details
	private Vector<Vector<Object>> convertToVector(List<Employee> employees) {
		// vector of Employee objects
		Vector<Vector<Object>> allEmployee = new Vector<Vector<Object>>();
		for (Employee e : model.getAll()) {
			Vector<Object> employeeDetailsPanel = new Vector<>();
			employeeDetailsPanel.add(e.getEmployeeId());
			employeeDetailsPanel.add(e.getPpsNumber());
			employeeDetailsPanel.add(e.getSurname());
			employeeDetailsPanel.add(e.getFirstName());
			employeeDetailsPanel.add(e.getGender());
			employeeDetailsPanel.add(e.getDepartment());
			employeeDetailsPanel.add(e.getSalary());
			employeeDetailsPanel.add(e.getFullTime());
			allEmployee.add(employeeDetailsPanel);
		}

		return allEmployee;
	}// end getAllEmployees

	// activate field for editing
	private void editDetails() {
		// activate field for editing if there is records to display
		if (isSomeoneToDisplay()) {
			// remove euro sign from salary text field
			salaryField.setText(fieldFormat.format(currentEmployee.getSalary()));
			change = false;
			setEnabled(true);// enable text fields for editing
		} // end if
	}// end editDetails

	// ignore changes and set text field unenabled
	private void cancelChange() {
		setEnabled(false);
		displayRecords(currentEmployee);
	}// end cancelChange

	// check if any of records in file is active - ID is not 0
	private boolean isSomeoneToDisplay() {
		boolean someoneToDisplay = !model.getAll().isEmpty();

		if (!someoneToDisplay) {
			currentEmployee = null;
			idField.setText("");
			ppsNumberField.setText("");
			surnameField.setText("");
			firstNameField.setText("");
			salaryField.setText("");
			genderCombo.setSelectedIndex(0);
			departmentCombo.setSelectedIndex(0);
			fullTimeCombo.setSelectedIndex(0);
			JOptionPane.showMessageDialog(null, "No Employees registered!");
		}
		return someoneToDisplay;
	}

	// check for correct PPS format and look if PPS already in use
	public boolean ppsNumberAlreadyExists(String ppsNumber, long currentByte) {
		return model.getAll().stream().anyMatch(e -> e.getPpsNumber().equalsIgnoreCase(ppsNumber));
	}

	// check if file name has extension .dat
	private boolean checkFileName(File fileName) {
		boolean checkFile = false;
		int length = fileName.toString().length();

		// check if last characters in file name is .dat
		if (fileName.toString().charAt(length - 4) == '.' && fileName.toString().charAt(length - 3) == 'd'
				&& fileName.toString().charAt(length - 2) == 'a' && fileName.toString().charAt(length - 1) == 't')
			checkFile = true;
		return checkFile;
	}// end checkFileName

	// check if any changes text field where made
	private boolean checkForChanges() {
		boolean anyChanges = false;
		// if changes where made, allow user to save there changes
		if (change) {
			saveChanges();// save changes
			anyChanges = true;
		} // end if
			// if no changes made, set text fields as unenabled and display
			// current Employee
		else {
			setEnabled(false);
			displayRecords(currentEmployee);
		} // end else

		return anyChanges;
	}// end checkForChanges

	// check for input in text fields
	private boolean checkInput() {
		boolean valid = true;
		// if any of inputs are in wrong format, colour text field and display
		// message
		if (ppsNumberField.isEditable() && ppsNumberField.getText().trim().isEmpty()) {
			ppsNumberField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (surnameField.isEditable() && surnameField.getText().trim().isEmpty()) {
			surnameField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (firstNameField.isEditable() && firstNameField.getText().trim().isEmpty()) {
			firstNameField.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (genderCombo.getSelectedIndex() == 0 && genderCombo.isEnabled()) {
			genderCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		if (departmentCombo.getSelectedIndex() == 0 && departmentCombo.isEnabled()) {
			departmentCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
		try {// try to get values from text field
			Double.parseDouble(salaryField.getText());
			// check if salary is greater than 0
			if (Double.parseDouble(salaryField.getText()) < 0) {
				salaryField.setBackground(new Color(255, 150, 150));
				valid = false;
			} // end if
		} // end try
		catch (NumberFormatException num) {
			if (salaryField.isEditable()) {
				salaryField.setBackground(new Color(255, 150, 150));
				valid = false;
			} // end if
		} // end catch
		if (fullTimeCombo.getSelectedIndex() == 0 && fullTimeCombo.isEnabled()) {
			fullTimeCombo.setBackground(new Color(255, 150, 150));
			valid = false;
		} // end if
			// display message if any input or format is wrong
		if (!valid)
			JOptionPane.showMessageDialog(null, "Wrong values or format! Please check!");
		// set text field to white colour if text fields are editable
		if (ppsNumberField.isEditable())
			setToWhite();

		return valid;
	}

	// set text field background colour to white
	private void setToWhite() {
		ppsNumberField.setBackground(UIManager.getColor("TextField.background"));
		surnameField.setBackground(UIManager.getColor("TextField.background"));
		firstNameField.setBackground(UIManager.getColor("TextField.background"));
		salaryField.setBackground(UIManager.getColor("TextField.background"));
		genderCombo.setBackground(UIManager.getColor("TextField.background"));
		departmentCombo.setBackground(UIManager.getColor("TextField.background"));
		fullTimeCombo.setBackground(UIManager.getColor("TextField.background"));
	}// end setToWhite

	// enable text fields for editing
	public void setEnabled(boolean booleanValue) {
		boolean search;
		if (booleanValue)
			search = false;
		else
			search = true;
		ppsNumberField.setEditable(booleanValue);
		surnameField.setEditable(booleanValue);
		firstNameField.setEditable(booleanValue);
		genderCombo.setEnabled(booleanValue);
		departmentCombo.setEnabled(booleanValue);
		salaryField.setEditable(booleanValue);
		fullTimeCombo.setEnabled(booleanValue);
		saveChange.setVisible(booleanValue);
		cancelChange.setVisible(booleanValue);
		searchByIdField.setEnabled(search);
		searchBySurnameField.setEnabled(search);
		searchId.setEnabled(search);
		searchSurname.setEnabled(search);
	}// end setEnabled

	// open file
	private void openFile() {
		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open");
		fc.setFileFilter(datfilter);

		// If there are unsaved changes, prompt user
		if (file.length() != 0 || change) {
			int returnVal = JOptionPane.showOptionDialog(frame, "Do you want to save changes?", "Save",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
			if (returnVal == JOptionPane.YES_OPTION) {
				saveFile();
			}
		}

		int returnVal = fc.showOpenDialog(EmployeeDetails.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File newFile = fc.getSelectedFile();

			// Delete auto-generated file if necessary
			if (file.getName().equals(generatedFileName)) {
				file.delete();
			}

			file = newFile; // Update the current file
			model.loadAllFromFile(file.getAbsolutePath()); // Load employees from file
			if (!model.getAll().isEmpty()) {
				currentEmployee = model.getAll().get(0); // Display first employee
			}
			displayRecords(currentEmployee);
		}
	}

	// save file
	private void saveFile() {
		if (file.getName().equals(generatedFileName)) {
			saveFileAs();
		} else {
			if (change) {
				int returnVal = JOptionPane.showOptionDialog(
						frame, "Do you want to save changes?", "Save",
						JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

				if (returnVal == JOptionPane.YES_OPTION) {
					// Ensure a valid employee exists before saving
					if (!idField.getText().equals("")) {
						saveCurrentEmployee();
					}
				}
			}

			displayRecords(currentEmployee);
			setEnabled(false);
		}
	}

	// save changes to current Employee
	private void saveChanges() {
		int returnVal = JOptionPane.showOptionDialog(
				frame, "Do you want to save changes to current Employee?", "Save",
				JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

		if (returnVal == JOptionPane.YES_OPTION) {
			saveCurrentEmployee();
		}

		displayRecords(currentEmployee);
		setEnabled(false);
	}

	// save file as 'save as'
	private void saveFileAs() {
		final JFileChooser fc = new JFileChooser();
		String defaultFileName = "new_Employee.dat";

		fc.setDialogTitle("Save As");
		fc.setFileFilter(datfilter);
		fc.setApproveButtonText("Save");
		fc.setSelectedFile(new File(defaultFileName));

		int returnVal = fc.showSaveDialog(EmployeeDetails.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File newFile = fc.getSelectedFile();

			// Ensure the file has a .dat extension
			if (!checkFileName(newFile)) {
				newFile = new File(newFile.getAbsolutePath() + ".dat");
			}

			// Delegate file handling to EmployeeDataModel
			model.loadAllFromFile(file.getAbsolutePath());

			try {
				Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				if (file.getName().equals(generatedFileName)) {
					file.delete(); // Delete the auto-generated file
				}
				file = newFile; // Set the new file as the active one
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Error saving file.", "Save Error", JOptionPane.ERROR_MESSAGE);
			}
		}

		changesMade = false;
	}

	private void saveCurrentEmployee() {
		Employee newDetails = getChangedDetails();
		model.updateEmployee(currentEmployee, newDetails, file.getAbsolutePath());
		changesMade = false;
	}

	// allow to save changes to file when exiting the application
	private void exitApp() {
		int returnVal = JOptionPane.showConfirmDialog(
				frame, "Do you want to save changes before exiting?", "Exit",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (returnVal == JOptionPane.YES_OPTION) {
			saveFile();
			System.exit(0);
		} else if (returnVal == JOptionPane.NO_OPTION) {
			System.exit(0);
		}
	}

	// generate 20 character long file name
	private String getFileName() {
		String fileNameChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_-";
		StringBuilder fileName = new StringBuilder();
		Random rnd = new Random();
		// loop until 20 character long file name is generated
		while (fileName.length() < 20) {
			int index = (int) (rnd.nextFloat() * fileNameChars.length());
			fileName.append(fileNameChars.charAt(index));
		}
		String generatedfileName = fileName.toString();
		return generatedfileName;
	}// end getFileName

	// create file with generated file name when application is opened
	private void createRandomFile() {
		generatedFileName = getFileName() + ".dat";
		// assign generated file name to file
		file = new File(generatedFileName);
		// create file
		model.loadAllFromFile(file.getAbsolutePath());
	}// end createRandomFile

	// action listener for buttons, text field and menu items
	public void actionPerformed(ActionEvent e) {
		if (!checkInput() || checkForChanges()) {
			return;
		}

		Object source = e.getSource();

		if (source == closeApp) {
			exitApp();
		} else if (source == open) {
			openFile();
		} else if (source == save) {
			saveFile();
			change = false;
		} else if (source == saveAs) {
			saveFileAs();
			change = false;
		} else if (source == searchById || source == searchByIdField) {
			openSearchDialog(new SearchByIdDialogFactory());
		} else if (source == searchSurname || source == searchBySurnameField) {
			openSearchDialog(new SearchBySurnameDialogFactory());
		} else if (source == saveChange) {
			saveChanges();
		} else if (source == cancelChange) {
			cancelChange();
		} else if (source == firstItem || source == first) {
			navigateAndDisplay(() -> firstRecord());
		} else if (source == prevItem || source == previous) {
			navigateAndDisplay(() -> previousRecord());
		} else if (source == nextItem || source == next) {
			navigateAndDisplay(() -> nextRecord());
		} else if (source == lastItem || source == last) {
			navigateAndDisplay(() -> lastRecord());
		} else if (source == listAll || source == displayAll) {
			displayEmployeeSummaryDialog();
		} else if (source == create || source == add) {
			openDialog(new AddDialogFactory());
		} else if (source == modify || source == edit) {
			editDetails();
		} else if (source == delete || source == deleteButton) {
			deleteRecord();
		}
	}// end actionPerformed

	private void openSearchDialog(DialogFactory factory) {
		JDialog dialog = factory.createDialog(this);
		dialog.setVisible(true);
	}

	private void navigateAndDisplay(Runnable navigationMethod) {
		navigationMethod.run();
		displayRecords(currentEmployee);
	}

	private void openDialog(DialogFactory factory) {
		JDialog dialog = factory.createDialog(this);
		dialog.setVisible(true);
	}

	private void displayEmployeeSummaryDialog() {
		List<Employee> employees = model.getAll(); // Fetch employees from the model

		if (employees.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No Employees registered!");
		} else {
			Vector<Vector<Object>> employeeData = convertToVector(employees);
			new EmployeeSummaryDialog(employeeData);
		}
	}

	// content pane for main dialog
	private void createContentPane() {
		setTitle("Employee Details");
		createRandomFile();// create random file name
		JPanel dialog = new JPanel(new MigLayout());

		setJMenuBar(menuBar());// add menu bar to frame
		// add search panel to frame
		dialog.add(searchPanel(), "width 400:400:400, growx, pushx");
		// add navigation panel to frame
		dialog.add(navigPanel(), "width 150:150:150, wrap");
		// add button panel to frame
		dialog.add(buttonPanel(), "growx, pushx, span 2,wrap");
		// add details panel to frame
		dialog.add(detailsPanel(), "gap top 30, gap left 150, center");

		JScrollPane scrollPane = new JScrollPane(dialog);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		addWindowListener(this);
	}// end createContentPane

	// create and show main dialog
	private static void createAndShowGUI() {

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.createContentPane();// add content pane to frame
		frame.setSize(760, 600);
		frame.setLocation(250, 200);
		frame.setVisible(true);
	}// end createAndShowGUI

	// main method
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}// end main

	// DocumentListener methods
	public void changedUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	public void insertUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	public void removeUpdate(DocumentEvent d) {
		change = true;
		new JTextFieldLimit(20);
	}

	// ItemListener method
	public void itemStateChanged(ItemEvent e) {
		change = true;
	}

	// WindowsListener methods
	public void windowClosing(WindowEvent e) {
		// exit application
		exitApp();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}
}// end class EmployeeDetails
