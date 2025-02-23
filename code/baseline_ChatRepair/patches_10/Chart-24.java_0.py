public Paint getPaint(double value) {
    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);
    int g = (int) ((v - this.lowerBound) / (this.upperBound 
            - this.lowerBound) * 255.0);
    g = Math.max(0, Math.min(255, g)); // Additional safety clip, though should be unnecessary
    return new Color(g, g, g);
}
