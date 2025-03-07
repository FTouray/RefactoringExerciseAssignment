import javax.swing.JOptionPane;

public class SearchByIdCommand implements Command {
    private EmployeeDetails employeeDetails;
    private SearchContext searchContext;

    public SearchByIdCommand(EmployeeDetails employeeDetails, SearchContext searchContext) {
        this.employeeDetails = employeeDetails;
        this.searchContext = searchContext;
    }

    @Override
    public void execute() {
        String query = employeeDetails.searchByIdField.getText().trim();

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter an ID to search.");
            return;
        }

        // Execute search strategy directly
        searchContext.setStrategy(new SearchByIdStrategy());
        searchContext.executeSearch(employeeDetails, query);
    }
    
}
