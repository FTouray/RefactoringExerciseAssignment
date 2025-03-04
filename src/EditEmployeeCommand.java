public class EditEmployeeCommand implements Command {
    private EmployeeDetails employeeDetails;

    public EditEmployeeCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.editDetails(); 
    }
}
