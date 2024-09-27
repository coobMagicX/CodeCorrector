public Dfp multiply(final int x) {
    // Check if x is a large value that might lead to overflow
    if (x > 10000) {
        // Handle the large value by scaling the Dfp object
        return multiplyFast(x / 10000).multiplyFast(10000);
    } else if (x < -10000) {
        // Handle the large negative value by scaling the Dfp object
        return multiplyFast(x / 10000).multiplyFast(10000);
    } else {
        // Proceed with normal multiplication
        return multiplyFast(x);
    }
}