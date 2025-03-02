
import javax.swing.JDialog;

public class AddDialogFactory extends DialogFactory {
    @Override
    public JDialog createDialog(EmployeeDetails parent) {
        return new AddRecordDialog(parent);
    }
}
