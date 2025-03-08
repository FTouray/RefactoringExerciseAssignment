package strategy;
import javax.swing.JOptionPane;

import ui.EmployeeDetails;

public class SearchBySurnameStrategy implements SearchStrategy {
    @Override
    public void search(EmployeeDetails employeeDetails, String query) {
        boolean found = false;

        if (employeeDetails.isSomeoneToDisplay()) {
            employeeDetails.triggerFirstRecord();
            String firstSurname = employeeDetails.getCurrentEmployee().getSurname().trim();

            do {
                if (query.trim().equalsIgnoreCase(employeeDetails.getCurrentEmployee().getSurname().trim())) {
                    found = true;
                    employeeDetails.displayRecords(employeeDetails.getCurrentEmployee());
                    break;
                }
                employeeDetails.triggerNextRecord();
            } while (!firstSurname.equalsIgnoreCase(employeeDetails.getCurrentEmployee().getSurname().trim()));

            if (!found) {
                JOptionPane.showMessageDialog(null, "Employee not found!");
            }
        }
    }
}
