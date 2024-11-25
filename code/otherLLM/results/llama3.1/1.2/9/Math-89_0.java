public void addValue(Object v) {
    if (v instanceof Comparable) {  // Verify if input value is Comparable
        addValue((Comparable<?>) v);  // Proceed with adding the comparable value
    } else {
        throw new RuntimeException("Cannot add non-comparable value");  // Throw exception for non-comparable values
    }
}