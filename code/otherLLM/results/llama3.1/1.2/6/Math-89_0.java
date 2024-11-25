public void addValue(Object v) {
    if (v instanceof Comparable && !(v instanceof String)) {  // Ensure correct parameter validation before casting to Comparable.
        addValue((Comparable<?>) v);
    } else if (v instanceof Character) {  // Update the addValue method to correctly handle non-comparable objects.
        addValue(Character.valueOf(v));
    }
}