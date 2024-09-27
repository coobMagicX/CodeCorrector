public double getPct(Object v) {
    Iterator<Comparable<?>> iterator = valuesIterator();
    while (iterator.hasNext()) {
        Comparable<?> current = iterator.next();
        if (current.equals(v)) {
            return getCumPct(current);
        }
    }
    return 0.0; // Return 0.0 if the value is not found in the frequency table
}