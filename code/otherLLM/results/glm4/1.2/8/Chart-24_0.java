public Paint getPaint(double value) {
    double v = Math.max(value, this.lowerBound); // Ensuring v is not less than lowerBound
    v = Math.min(v, this.upperBound); // Ensuring v does not exceed upperBound

    // Adjusting calculation to prevent overflow and ensure correct scaling
    double scale = 255.0 / (this.upperBound - this.lowerBound);
    int g = (int) Math.round((v - this.lowerBound) * scale);

    // Ensure the grayscale value is within the valid range [0, 255]
    g = Math.max(0, Math.min(g, 255));

    return new Color(g, g, g); // Producing a grayscale color
}