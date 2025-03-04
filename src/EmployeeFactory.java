import java.io.File;

public class EmployeeFactory {

    private static final RandomFile application = RandomFile.getInstance();
    private static String filePath;
    
        /**
         * A SHORTER overload: for brand-new employees or cases
         * where you DON'T need to skip an existing record.
         */
        public static Employee createEmployee(
                int employeeId,
                String ppsNumber,
                String surname,
                String firstName,
                char gender,
                String department,
                double salary,
                boolean fullTime) {
            // Calls the "full" method with a default -1 for currentByte
            return createEmployee(
                    employeeId,
                    ppsNumber,
                    surname,
                    firstName,
                    gender,
                    department,
                    salary,
                    fullTime,
                    -1 // no skipping needed
            );
        }
    
        /**
         * The FULL overload:
         * includes 'long currentByte' for skipping the record
         * at this position if we are editing or ignoring an existing employee.
         */
        public static Employee createEmployee(
                int employeeId,
                String ppsNumber,
                String surname,
                String firstName,
                char gender,
                String department,
                double salary,
                boolean fullTime,
                long currentByte // pass actual position or -1 if not applicable
        ) {
            // 1. Provide a default for department if needed
            if (department == null || department.trim().isEmpty()) {
                department = "Administration";
            }
            // 2. Uppercase / trim
            String ppsN = (ppsNumber == null) ? "" : ppsNumber.toUpperCase().trim();
            String sur = (surname == null) ? "" : surname.toUpperCase().trim();
            String first = (firstName == null) ? "" : firstName.toUpperCase().trim();
    
            // 3. Salary check
            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative: " + salary);
            }
    
            // 4. Validate PPS format & skip existing record if needed
            if (!checkPpsAvailability(ppsN, currentByte)) {
                // Here we unify the "format check" and "already in use" logic
                throw new IllegalArgumentException("Invalid or already-used PPS: " + ppsN);
            }
    
            // 5. Validate gender
            if (gender != 'M' && gender != 'F') {
                throw new IllegalArgumentException("Gender must be M or F. Got: " + gender);
            }
    
            // 6. Return the final constructed Employee
            return new Employee(
                    employeeId,
                    ppsN,
                    sur,
                    first,
                    gender,
                    department.trim(),
                    salary,
                    fullTime);
        }
    
        // If you have an "empty employee" concept, you can still keep it:
        public static Employee createEmptyEmployee() {
            return new Employee(0, "", "", "", '\0', "", 0.0, false);
        }
    
        /**
         * Example logic combining:
         * - PPS length & format checks
         * - Checking if PPS is already in the file
         */
        private static boolean checkPpsAvailability(String ppsNumber, long currentByte) {
            // Basic format check
            if (!isFormatValid(ppsNumber)) {
                return false;
            }
            // Check the file to see if PPS is used, skipping 'currentByte' if necessary
            if (filePath == null || filePath.isEmpty()) {
                System.out.println("ERROR: File path not set!");
                return true;
            }

            // Check if the file actually exists
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("⚠️ WARNING: File does not exist yet. Assuming PPS is available.");
            return true; // Assume PPS is available if file doesn't exist
        }
            
            try {
                                application.openReadFile(filePath);
                boolean used = application.isPpsExist(ppsNumber, currentByte);
                application.closeReadFile();
                return !used; // if used == true => availability is false
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    
        private static boolean isFormatValid(String pps) {
            // e.g., must be length 8 or 9, first 7 must be digits, last must be letter,
            // etc.
            if (!(pps.length() == 8 || pps.length() == 9)) {
                return false;
            }
            if (!(Character.isDigit(pps.charAt(0)) && Character.isDigit(pps.charAt(1))
                    && Character.isDigit(pps.charAt(2)) && Character.isDigit(pps.charAt(3))
                    && Character.isDigit(pps.charAt(4)) && Character.isDigit(pps.charAt(5))
                    && Character.isDigit(pps.charAt(6)) && Character.isLetter(pps.charAt(7))
                    && (pps.length() == 8 || Character.isLetter(pps.charAt(8))))) {
                return false;
            }
            return true;
        }
    
        public static void setFilePath(String path) {
            filePath = path;
    }

}
