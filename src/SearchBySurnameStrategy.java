import javax.swing.JOptionPane;

public class SearchBySurnameStrategy implements SearchStrategy {
    @Override
    public void search(EmployeeDetails employeeDetails, String query) {
        boolean found = false;

        if (employeeDetails.isSomeoneToDisplay()) {
            employeeDetails.triggerFirstRecord();
            String firstSurname = employeeDetails.currentEmployee.getSurname().trim();

            do {
                if (query.trim().equalsIgnoreCase(employeeDetails.currentEmployee.getSurname().trim())) {
                    found = true;
                    employeeDetails.displayRecords(employeeDetails.currentEmployee);
                    break;
                }
                employeeDetails.triggerNextRecord();
            } while (!firstSurname.equalsIgnoreCase(employeeDetails.currentEmployee.getSurname().trim()));

            if (!found) {
                JOptionPane.showMessageDialog(null, "Employee not found!");
            }
        }
    }
}
