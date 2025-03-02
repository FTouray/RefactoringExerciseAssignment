public class IdSearchStrategy implements SearchStrategy {
    @Override
    public boolean matches(Employee e, String searchValue) {
        try {
            int idToFind = Integer.parseInt(searchValue.trim());
            return e.getEmployeeId() == idToFind;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
