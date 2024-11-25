public double getPct(Object v) {
    if (v instanceof Integer) { // Check if the object is an instance of Integer
        int intValue = (Integer) v; // Perform type casting to Integer
        int total = 100; // Assuming we are calculating a percentage out of 100 for simplicity
        return (double) intValue / total * 100;
    } else {
        throw new IllegalArgumentException("Input must be an instance of Integer");
    }
}