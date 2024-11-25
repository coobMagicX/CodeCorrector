public Paint getPaint(double value) {
    double lowerBound = this.lowerBound;
    double upperBound = this.upperBound;

    // Ensure that the bounds are not negative and that the upper bound is greater than or equal to the lower bound.
    if (lowerBound < 0) lowerBound = 0;
    if (upperBound > value) upperBound = value; // This line may need adjustment based on exact requirements.

    double v = Math.max(value, lowerBound);
    v = Math.min(v, upperBound);

    int g = (int) ((v - lowerBound) / (upperBound - lowerBound) * 255.0);
    
    // Check if value is outside the defined bounds and handle accordingly
    if (value < lowerBound || value > upperBound) {
        // Handle the error or edge case, here returning white color for values above upper bound.
        // This line may need adjustment based on exact requirements.
        return new Color(255, 255, 255);
    }

    return new Color(g, g, g);
}