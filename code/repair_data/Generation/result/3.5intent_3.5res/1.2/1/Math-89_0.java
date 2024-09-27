public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Input object must be of Comparable type");
    }
    addValue((Comparable<?>) v);
}