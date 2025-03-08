package strategy;
import ui.EmployeeDetails;

public interface SearchStrategy {
    void search(EmployeeDetails employeeDetails, String query);
}
