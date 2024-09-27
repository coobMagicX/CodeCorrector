public void addValue(Object v) {
    if (!(v instanceof Comparable)) {
        throw new IllegalArgumentException("Object is not of Comparable type");
    }
    addValue((Comparable<?>) v);            
}