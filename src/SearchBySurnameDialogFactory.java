import javax.swing.JDialog;

public class SearchBySurnameDialogFactory extends DialogFactory {
    @Override
    public JDialog createDialog(EmployeeDetails parent) {
        return new SearchBySurnameDialog(parent);
    }
}