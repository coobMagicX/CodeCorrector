public double percentageValue() {
    long intermediateResult = multiply(100).longValue(); // Perform multiplication with a large enough data type to prevent overflow.
    return (double) intermediateResult / 1e15; // Divide by a large power of 10 to accurately round the result and prevent loss of precision.
}