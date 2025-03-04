public class DeleteRecordCommand implements Command {
    private EmployeeDetails employeeDetails;

    public DeleteRecordCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.triggerDeleteRecord(); 
    }
}
