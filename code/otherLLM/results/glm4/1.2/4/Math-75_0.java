public double getPct(Object v) {
    if (v instanceof Comparable) {
        return getCumPct((Comparable<?>) v);
    }
    throw new IllegalArgumentException("The provided object is not a comparable type.");
}

// Note: This modification includes an instance check to ensure that the object passed to the method
// implements the Comparable interface before casting. If it does not, an IllegalArgumentException is thrown.