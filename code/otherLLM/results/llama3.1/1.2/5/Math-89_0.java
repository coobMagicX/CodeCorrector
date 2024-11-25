public void addValue(Object v) {
    if (v instanceof Comparable) {
        addValue((Comparable<?>) v);
    } else {
        // Handle non-comparable objects
        throw new RuntimeException("Cannot add non-comparable object: " + v.getClass().getName());
    }
}