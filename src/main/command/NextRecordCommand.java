package command;
import ui.EmployeeDetails;

public class NextRecordCommand implements Command {
    private EmployeeDetails employeeDetails;

    public NextRecordCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.triggerNextRecord(); // Moves to the next employee
        employeeDetails.displayRecords(employeeDetails.getCurrentEmployee());
    }
}
