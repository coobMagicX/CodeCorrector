public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Added value must implement Comparable interface");
    }
    addValue((Comparable<?>) v);            
}