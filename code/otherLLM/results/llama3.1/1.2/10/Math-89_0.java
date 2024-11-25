public void addValue(Object v) {
    if (v != null && v instanceof Comparable) {
        addValue((Comparable)v);            
} else {
    // handle non-comparable type, e.g., throw an exception or log a warning
    throw new IllegalArgumentException("Added value must be comparable");
}