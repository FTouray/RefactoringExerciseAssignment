import javax.swing.JOptionPane;

public class SearchContext {
    private SearchStrategy strategy;

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeSearch(EmployeeDetails employeeDetails, String query) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set");
        }

        if (query == null || query.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Search query cannot be empty.");
            return;
        }

        strategy.search(employeeDetails, query);
    }
}
