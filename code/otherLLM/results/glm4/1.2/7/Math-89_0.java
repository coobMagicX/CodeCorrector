public void addValue(Object v) {
    if (v instanceof Comparable<?>) {
        addValue((Comparable<?>) v);
    } else {
        throw new IllegalArgumentException("The provided object is not castable to Comparable.");
    }
}