import javax.swing.JOptionPane;

public class SearchByIdCommand implements Command {
    private EmployeeDetails employeeDetails;
    private SearchContext searchContext;
    private boolean openDialog;

    public SearchByIdCommand(EmployeeDetails employeeDetails, SearchContext searchContext, boolean openDialog) {
        this.employeeDetails = employeeDetails;
        this.searchContext = searchContext;
        this.openDialog = openDialog;
    }

    @Override
    public void execute() {
        if (openDialog) {
            new SearchByIdDialog(employeeDetails, searchContext); // Open search dialog
        } else {
            // Get the ID directly from the search field
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
    
}
