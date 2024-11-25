public Paint getPaint(double value) {
    double lowerBound = this.getLowerBound();
    double upperBound = this.getUpperBound();

    double v = Math.max(value, lowerBound);
    v = Math.min(v, upperBound);

    // Adjust the logic to handle values outside the defined bounds by setting them to either
    // the lower or upper bound before calculating the grayscale value.
    int g;
    if (value < lowerBound) {
        g = 0; // Set grayscale to black for values below the lower bound
    } else if (value > upperBound) {
        g = 255; // Set grayscale to white for values above the upper bound
    } else {
        g = (int) ((value - lowerBound) / (upperBound - lowerBound) * 255.0);
    }

    return new Color(g, g, g);
}