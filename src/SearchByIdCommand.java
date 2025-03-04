public class SearchByIdCommand implements Command {
    private EmployeeDetails employeeDetails;

    public SearchByIdCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.triggerSearchByIdDialog();
    }
}
