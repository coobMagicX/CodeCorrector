public double getPct(Object v) {
    if (v instanceof Integer) {
        return (double) ((Integer) v) / getTotalElements();
    } else {
        throw new IllegalArgumentException("Expected an Integer object");
    }
}

private int getTotalElements() {
    // Assuming there is a method to get the total number of elements in the collection
    // This method needs to be implemented based on your class context
    return 0;
}