public Paint getPaint(double value) {
    // bound checking: Ensure that the input value is within the bounds [0.0, 1.0]
    if (value < this.lowerBound || value > this.upperBound) {
        throw new IllegalArgumentException("Input value must be within the bounds [0.0, 1.0]");
    }
    
    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);
    int g = (int) ((value - this.lowerBound) / (this.upperBound 
            - this.lowerBound) * 255.0);
    return new Color(g, g, g);
}