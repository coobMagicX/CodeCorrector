public Dfp multiply(final int x) {
    // Assuming Dfp class has a method to convert an integer to a Dfp instance and a method for multiplication.
    // Replace 'convertToDfp' with the actual method name used in your Dfp class that converts an integer to Dfp.
    final Dfp dp = convertToDfp(x);
    return multiplyFast(dp);  // Use the dp instance to perform multiplication
}