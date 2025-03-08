package strategy;
import javax.swing.JOptionPane;

import ui.EmployeeDetails;

public class SearchByIdStrategy implements SearchStrategy {
    @Override
    public void search(EmployeeDetails employeeDetails, String query) {
        boolean found = false;

        try {
            int searchId = Integer.parseInt(query.trim());

            if (employeeDetails.isSomeoneToDisplay()) {
                employeeDetails.triggerFirstRecord();
                int firstId = employeeDetails.getCurrentEmployee().getEmployeeId();

                do {
                    if (searchId == employeeDetails.getCurrentEmployee().getEmployeeId()) {
                        found = true;
                        employeeDetails.displayRecords(employeeDetails.getCurrentEmployee());
                        break;
                    }
                    employeeDetails.triggerNextRecord();
                } while (firstId != employeeDetails.getCurrentEmployee().getEmployeeId());

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
