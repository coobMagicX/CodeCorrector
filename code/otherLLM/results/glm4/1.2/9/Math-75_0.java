public double getPct(Object v) {
    // First, check if the passed object is null.
    if (v == null) {
        throw new IllegalArgumentException("Value cannot be null");
    }
    
    // Then, cast it to an Integer assuming it's a numeric type we can work with for percentage calculation.
    int value;
    try {
        value = ((Number) v).intValue();
    } catch (ClassCastException e) {
        // If the object is not a Number or cannot be cast to an Integer, throw an IllegalArgumentException.
        throw new IllegalArgumentException("Value must be a numeric type", e);
    }
    
    return getCumPct(value);
}