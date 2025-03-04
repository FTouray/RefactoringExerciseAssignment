public class NextRecordCommand implements Command {
    private EmployeeDetails employeeDetails;

    public NextRecordCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.nextRecord(); // Moves to the next employee
        employeeDetails.displayRecords(employeeDetails.currentEmployee);
    }
}
