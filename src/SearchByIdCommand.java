public class SearchByIdCommand implements Command {
    private EmployeeDetails employeeDetails;

    public SearchByIdCommand(EmployeeDetails employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public void execute() {
        // Use SearchContext with SearchByIdStrategy
        SearchContext searchContext = new SearchContext(new SearchByIdStrategy(employeeDetails));
        searchContext.executeSearch(employeeDetails.searchByIdField.getText().trim());
    }
}
