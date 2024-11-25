public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Non-comparable value: " + v);
    }
    addValue((Comparable<?>) v);            
}