public double getPct(Object v) {
    if (v instanceof Integer) {
        int value = (int)v;
        if (value == 0) {
            return 0.0; // handle zero value edge case
        }
        long cumPct = getCumPct(value); // cast integer to comparable type
        return (double)cumPct / value * 100; // calculate percentage
    } else if (v instanceof Long) {
        long value = (long)v;
        if (value == 0) {
            return 0.0; // handle zero value edge case
        }
        long cumPct = getCumPct(value); // cast long to comparable type
        return (double)cumPct / value * 100; // calculate percentage
    } else {
        throw new IllegalArgumentException("Invalid input: " + v); // raise exception for invalid inputs
    }
}