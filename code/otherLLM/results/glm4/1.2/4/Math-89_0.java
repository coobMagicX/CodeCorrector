public void addValue(Object v) throws IllegalArgumentException {
    if (v == null || !(v instanceof Comparable)) {
        throw new IllegalArgumentException("Object to be added must implement Comparable");
    }
    // Assuming the rest of the method implementation is correct up to this point
    // For example:
    freqTable.put((Comparable<?>) v, /* some logic here */);
}