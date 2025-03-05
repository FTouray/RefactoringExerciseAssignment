import javax.swing.JOptionPane;

public class SearchByIdStrategy implements SearchStrategy {
    private EmployeeDetails employeeDetails;

    public SearchByIdStrategy(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void search(String query) {
        boolean found = false;

        try {
            if (employeeDetails.isSomeoneToDisplay()) {
                employeeDetails.triggerFirstRecord(); // Start at the first record
                int firstId = employeeDetails.currentEmployee.getEmployeeId();

                if (query.trim().equals(employeeDetails.currentEmployee.getEmployeeId() + "")) {
                    found = true;
                    employeeDetails.displayRecords(employeeDetails.currentEmployee);
                } else {
                    employeeDetails.triggerNextRecord();
                    while (firstId != employeeDetails.currentEmployee.getEmployeeId()) {
                        if (query.trim().equals(employeeDetails.currentEmployee.getEmployeeId() + "")) {
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in searching by ID.");
        }
    }
}
