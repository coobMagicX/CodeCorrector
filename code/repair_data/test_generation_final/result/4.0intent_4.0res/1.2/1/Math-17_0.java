public Dfp multiply(final int x) {
    // Convert the integer to a Dfp to handle large values and potential overflow with precision
    Dfp dfpValue = new Dfp(x); // Assuming Dfp class has a constructor that accepts an integer
    return multiply(dfpValue); // Utilize the existing multiply method for Dfp objects
}