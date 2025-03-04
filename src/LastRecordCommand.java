public class LastRecordCommand {
    private EmployeeDetails employeeDetails;

    public LastRecordCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    public void execute() {
        employeeDetails.triggerLastRecord();
        employeeDetails.displayRecords(employeeDetails.currentEmployee);
    }
}
