public void addValue(Object v) {
    if (v instanceof Comparable) {
        addValue((Comparable<?>) v);
    } else {
        // handle non-comparable object, e.g., throw an exception
        throw new UnsupportedOperationException("Cannot add non-comparable value");
    }
}