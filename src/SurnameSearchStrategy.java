public class SurnameSearchStrategy implements SearchStrategy {
    @Override
    public boolean matches(Employee e, String searchValue) {
        return e.getSurname().equalsIgnoreCase(searchValue.trim());
    }
}
