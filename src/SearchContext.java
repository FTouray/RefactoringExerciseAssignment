public class SearchContext {
    private SearchStrategy strategy;

    public SearchContext(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeSearch(String query) {
        strategy.search(query);
    }
}
