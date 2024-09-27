public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Object must be of Comparable type");
    }
    addValue((Comparable<?>) v);
}