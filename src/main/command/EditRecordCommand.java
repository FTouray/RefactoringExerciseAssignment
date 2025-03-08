package command;
import ui.EmployeeDetails;

public class EditRecordCommand implements Command {
    private EmployeeDetails employeeDetails;

    public EditRecordCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.triggerEditDetails(); 
    }
}
