package command;

import ui.AddRecordDialog;
import ui.EmployeeDetails;

public class AddRecordCommand implements Command {
    private EmployeeDetails employeeDetails;

    public AddRecordCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        new AddRecordDialog(employeeDetails); // Opens the dialog for adding a record
    }
}
