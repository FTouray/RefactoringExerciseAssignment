public class FirstRecordCommand implements Command {
    private EmployeeDetails employeeDetails;

    public FirstRecordCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        employeeDetails.triggerFirstRecord();
        employeeDetails.displayRecords(employeeDetails.currentEmployee);
    }
}
