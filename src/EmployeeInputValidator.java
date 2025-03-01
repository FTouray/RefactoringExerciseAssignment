public class EmployeeInputValidator {

    /**
     * Checks if PPS Number is valid based on your assignment’s rules:
     * - Typically 7 digits + 1 letter, or 7 digits + 2 letters.
     */
    public static boolean isValidPps(String pps) {
        if (pps == null)
            return false;
        pps = pps.trim();
        // Must be length 8 or 9
        if (pps.length() < 8 || pps.length() > 9)
            return false;

        // First 7 characters should be digits
        for (int i = 0; i < 7; i++) {
            if (!Character.isDigit(pps.charAt(i))) {
                return false;
            }
        }

        // The 8th character must be a letter
        if (!Character.isLetter(pps.charAt(7))) {
            return false;
        }

        // If there's a 9th character, it should also be a letter
        if (pps.length() == 9 && !Character.isLetter(pps.charAt(8))) {
            return false;
        }
        return true;
    }

    /**
     * Checks if a string can be parsed as a non-negative salary (>= 0).
     */
    public static boolean isValidSalary(String salaryText) {
        if (salaryText == null)
            return false;
        try {
            double val = Double.parseDouble(salaryText.trim());
            return (val >= 0);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string is non-empty (not null or blank).
     */
    public static boolean isNotEmpty(String text) {
        return (text != null && !text.trim().isEmpty());
    }

}
