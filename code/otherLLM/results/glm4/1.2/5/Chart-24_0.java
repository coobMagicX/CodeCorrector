public Paint getPaint(double value) {
    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);

    // Check if the value is outside the specified bounds and apply clamping if necessary.
    if (value < this.lowerBound || value > this.upperBound) {
        // Optionally, you could also wrap or scale in a different way depending on requirements
        // For example: v = value % (this.upperBound - this.lowerBound) + this.lowerBound;
        // Or a scaled wrap: v = (value - this.lowerBound) / (this.upperBound - this.lowerBound) * 255.0;
    }

    // Scale the value to the range [0, 255]
    int g = (int) Math.round((v - this.lowerBound) / (this.upperBound - this.lowerBound) * 255.0);
    return new Color(g, g, g);
}