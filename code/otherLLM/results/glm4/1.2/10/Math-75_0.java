public double getPct(Object v) {
    if (v != null && v instanceof Comparable<?>) {
        return getCumPct((Comparable<?>) v);
    } else {
        // Handle the case where 'v' is not a valid comparable or is null.
        // Depending on requirements, you can return 0 or throw an exception.
        // For this example, let's return 0 for simplicity:
        return 0.0;
    }
}

// Assuming the method getCumPct is defined elsewhere in your context:
private double getCumPct(Comparable<?> value) {
    // Method implementation to calculate cumulative percentage
    // based on occurrences of 'value'
}