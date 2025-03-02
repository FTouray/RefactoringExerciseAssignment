public interface SearchStrategy {
    /**
     * Returns true if the given Employee 'e' matches the search criteria
     * described by 'searchValue'.
     */
    boolean matches(Employee e, String searchValue);
}
