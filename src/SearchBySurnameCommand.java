public class SearchBySurnameCommand implements Command {
    private EmployeeDetails employeeDetails;

    public SearchBySurnameCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.triggerSearchBySurnameDialog();
    }
}
