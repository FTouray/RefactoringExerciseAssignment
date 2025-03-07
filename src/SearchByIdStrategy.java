import javax.swing.JOptionPane;

public class SearchByIdStrategy implements SearchStrategy {
    @Override
    public void search(EmployeeDetails employeeDetails, String query) {
        boolean found = false;

        try {
            int searchId = Integer.parseInt(query.trim());

            if (employeeDetails.isSomeoneToDisplay()) {
                employeeDetails.triggerFirstRecord();
                int firstId = employeeDetails.currentEmployee.getEmployeeId();

                do {
                    if (searchId == employeeDetails.currentEmployee.getEmployeeId()) {
                        found = true;
                        employeeDetails.displayRecords(employeeDetails.currentEmployee);
                        break;
                    }
                    employeeDetails.triggerNextRecord();
                } while (firstId != employeeDetails.currentEmployee.getEmployeeId());

                if (!found) {
                    JOptionPane.showMessageDialog(null, "Employee not found!");
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid ID format! Please enter a number.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in searching by ID: " + e.getMessage());
        }
    }
}
