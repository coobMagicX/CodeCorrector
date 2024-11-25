public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Cannot add non-comparable object to frequency table");
    }
    addValue((Comparable<?>) v);            
}