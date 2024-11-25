public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Only comparable objects can be added");
    }
    addValue((Comparable)v);
}