import javax.swing.JOptionPane;

public class SearchBySurnameStrategy implements SearchStrategy {
    private EmployeeDetails employeeDetails;

    public SearchBySurnameStrategy(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void search(String query) {
        boolean found = false;

        if (employeeDetails.isSomeoneToDisplay()) {
            employeeDetails.triggerFirstRecord();
            String firstSurname = employeeDetails.currentEmployee.getSurname().trim();

            if (query.trim().equalsIgnoreCase(firstSurname)) {
                found = true;
                employeeDetails.displayRecords(employeeDetails.currentEmployee);
            } else {
                employeeDetails.triggerNextRecord();
                while (!firstSurname.equalsIgnoreCase(employeeDetails.currentEmployee.getSurname().trim())) {
                    if (query.trim().equalsIgnoreCase(employeeDetails.currentEmployee.getSurname().trim())) {
                        found = true;
                        employeeDetails.displayRecords(employeeDetails.currentEmployee);
                        break;
                    }
                    employeeDetails.triggerNextRecord();
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(null, "Employee not found!");
            }
        }
    }
}
