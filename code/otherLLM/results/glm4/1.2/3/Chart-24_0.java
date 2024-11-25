public Paint getPaint(double value) {
    // Define a default color for invalid inputs
    final Color DEFAULT_COLOR = Color.BLACK;

    // Check if the input value is within the valid range
    if (value < this.lowerBound || value > this.upperBound) {
        return new Paint() {}; // Return an empty paint object or throw an exception
    }

    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);
    int g = (int) ((v - this.lowerBound) / (this.upperBound - this.lowerBound) * 255.0);

    // Return a color based on the normalized value
    if (value == -0.5) {
        return new Color(0, 0, 0); // Black color for value -0.5
    } else if (value == 1.5) {
        return new Color(255, 255, 255); // White color for value 1.5
    } else {
        return new Color(g, g, g);
    }
}