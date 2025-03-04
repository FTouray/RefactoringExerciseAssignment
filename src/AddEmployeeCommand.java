public class AddEmployeeCommand implements Command {
    private EmployeeDetails employeeDetails;

    public AddEmployeeCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        new AddRecordDialog(employeeDetails); // Opens the dialog for adding a record
    }
}
