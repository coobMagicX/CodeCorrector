public void addValue(Object v) {
    if (v instanceof Comparable) { // Add type checking
        addValue((Comparable<?>) v);            
    } else {
        throw new IllegalArgumentException("Cannot add non-comparable object"); // Throw exception on invalid input
    }
}