import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class EmployeeDataModel {
    // In-memory list of employees
    private List<Employee> employees = new ArrayList<>();

    // Observer list
    private List<EmployeeModelObserver> observers = new ArrayList<>();

    // Register or remove observer
    public void registerObserver(EmployeeModelObserver obs) {
        observers.add(obs);
    }

    public void removeObserver(EmployeeModelObserver obs) {
        observers.remove(obs);
    }

    // Notify all observers
    private void notifyObservers() {
        for (EmployeeModelObserver obs : observers) {
            obs.onEmployeeListChanged();
        }
    }

    public void loadAllFromFile(String filePath) {
        employees.clear();
        RandomFile file = RandomFile.getInstance();
        file.openReadFile(filePath);

        long startPos = file.getFirst(); // position of first record
        Employee firstEmployee = file.readRecords(startPos);

        if (firstEmployee.getEmployeeId() > 0) {
            employees.add(firstEmployee);

            // Loop around untilback to startPos
            long currentPos = file.getNext(startPos);
            while (currentPos != startPos) {
                Employee e = file.readRecords(currentPos);
                if (e.getEmployeeId() > 0) {
                    employees.add(e);
                }
                currentPos = file.getNext(currentPos);
            }
        }

        file.closeReadFile();
        notifyObservers();
    }

    public List<Employee> getAll() {
        return employees;
    }

    public void addEmployee(Employee e, String filePath) {
        employees.add(e);

        RandomFile file = RandomFile.getInstance();
        file.openWriteFile(filePath);
        file.addRecords(e);
        file.closeWriteFile();

        notifyObservers();
    }

    // Remove employee from memory AND file
    public void removeEmployee(Employee e, String filePath) {
        employees.remove(e);

        long position = findEmployeePositionInFile(e, filePath);
        if (position >= 0) {
            RandomFile file = RandomFile.getInstance();
            file.openWriteFile(filePath);
            file.deleteRecords(position); 
            file.closeWriteFile();
        }

        notifyObservers();
    }

   
    public void updateEmployee(Employee oldEmployee, Employee newDetails, String filePath) {
        int index = employees.indexOf(oldEmployee);
        if (index >= 0) {
            employees.set(index, newDetails);
        } else {
            
        }

        long position = findEmployeePositionInFile(oldEmployee, filePath);
        if (position >= 0) {
            RandomFile file = RandomFile.getInstance();
            file.openWriteFile(filePath);
            file.changeRecords(newDetails, position);
            file.closeWriteFile();
        } else {
            JOptionPane.showMessageDialog(null, "Could not find old employee in file for updating!");
        }

        notifyObservers();
    }

    private long findEmployeePositionInFile(Employee e, String filePath) {
        RandomFile file = RandomFile.getInstance();
        file.openReadFile(filePath);

        long startPos = file.getFirst();
        long currentPos = startPos;

        // Read first
        Employee record = file.readRecords(currentPos);

        // If the first record matches, return now
        if (record.getEmployeeId() == e.getEmployeeId()) {
            file.closeReadFile();
            return currentPos;
        }
        
        // Else keep going
        long nextPos = file.getNext(currentPos);
        while (nextPos != startPos) {
            Employee nextRecord = file.readRecords(nextPos);
            if (nextRecord.getEmployeeId() == e.getEmployeeId()) {
                file.closeReadFile();
                return nextPos;
            }
            nextPos = file.getNext(nextPos);
        }

        file.closeReadFile();
        return -1; // not found
    }
}
