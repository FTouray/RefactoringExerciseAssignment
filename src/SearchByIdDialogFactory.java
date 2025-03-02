
import javax.swing.JDialog;

public class SearchByIdDialogFactory extends DialogFactory {
    @Override
    public JDialog createDialog(EmployeeDetails parent) {
        return new SearchByIdDialog(parent);
    }
}
