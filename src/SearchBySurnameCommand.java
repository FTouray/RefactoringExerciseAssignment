import javax.swing.JOptionPane;

public class SearchBySurnameCommand implements Command {
    private EmployeeDetails employeeDetails;
    private SearchContext searchContext;

    public SearchBySurnameCommand(EmployeeDetails employeeDetails, SearchContext searchContext) {
        this.employeeDetails = employeeDetails;
        this.searchContext = searchContext;
    }

    @Override
    public void execute() {
        String query = employeeDetails.searchBySurnameField.getText().trim();

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a surname to search.");
            return;
        }

        // Execute search strategy directly
        searchContext.setStrategy(new SearchBySurnameStrategy());
        searchContext.executeSearch(employeeDetails, query);
    }
}
