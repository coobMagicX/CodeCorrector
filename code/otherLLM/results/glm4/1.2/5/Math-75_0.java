public double getPct(Object v) {
    // Check if the object is a primitive int (Boxed int as Object Integer)
    if (v instanceof Integer) {
        return getCumPct((Comparable<?>) v);
    }
    // If it's not an instance of Integer, then assume it can be safely cast to Comparable<?> (e.g., primitive int)
    return getCumPct((Comparable<?>) v);
}