public class SearchBySurnameCommand implements Command {
    private EmployeeDetails employeeDetails;

    public SearchBySurnameCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        // Use SearchContext with SearchBySurnameStrategy
        SearchContext searchContext = new SearchContext(new SearchBySurnameStrategy(employeeDetails));
        searchContext.executeSearch(employeeDetails.searchBySurnameField.getText().trim());
    }
}
