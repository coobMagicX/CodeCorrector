public double getPct(Object v) {
    if (v instanceof Long) { // added explicit type checking
        return getCumPct((Long)v); // cast to Long for correct method call
    } else if (v instanceof Comparable<?>) { // added check for Comparable input
        return getCumPct((Comparable<?>) v);
    }
    throw new RuntimeException("Input must be of type Long or implement Comparable"); // added exception for unexpected input types
}