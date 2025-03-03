public interface EmployeeModelObserver {
    /**
     * Called whenever the employee list changes (added, removed, or updated).
     */
    void onEmployeeListChanged();
}
