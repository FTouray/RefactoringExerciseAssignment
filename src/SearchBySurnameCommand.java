import javax.swing.JOptionPane;

public class SearchBySurnameCommand implements Command {
    private EmployeeDetails employeeDetails;
    private SearchContext searchContext;
    private boolean openDialog;

    public SearchBySurnameCommand(EmployeeDetails employeeDetails, SearchContext searchContext, boolean openDialog) {
        this.employeeDetails = employeeDetails;
        this.searchContext = searchContext;
        this.openDialog = openDialog;
    }

    @Override
    public void execute() {
        if (openDialog) {
            new SearchBySurnameDialog(employeeDetails, searchContext);
        } else {
            // Get the surname directly from the search field
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
}
