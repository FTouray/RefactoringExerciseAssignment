public class DeleteEmployeeCommand implements Command {
    private EmployeeDetails employeeDetails;

    public DeleteEmployeeCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.deleteRecord(); 
    }
}
