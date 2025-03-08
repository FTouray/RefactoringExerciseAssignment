package command;
import ui.EmployeeDetails;

public class PreviousRecordCommand implements Command {
    private EmployeeDetails employeeDetails;

    public PreviousRecordCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.triggerPreviousRecord(); // Moves to the previous employee
        employeeDetails.displayRecords(employeeDetails.getCurrentEmployee());
    }
}
