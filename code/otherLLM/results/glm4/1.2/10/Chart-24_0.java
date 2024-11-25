public Paint getPaint(double value) {
    // Ensure that value is not negative, but still respect the lower bound if it's a positive number.
    double boundedValue = Math.max(0, Math.min(value, this.upperBound));
    
    // Calculate the grayscale component. The formula has been fixed to avoid division by zero or overflow.
    int g = (int) ((boundedValue - 0) / (this.upperBound - 0) * 255.0);
    // Clamp the value to ensure it does not exceed 255, which is the maximum value for an RGB component.
    g = Math.min(g, 255);

    return new Color(g, g, g);
}