public Paint getPaint(double value) {
    if (value < this.lowerBound) {
        return new Color(0, 0, 0); // Black for values below lower bound
    }
    if (value > this.upperBound) {
        return new Color(255, 255, 255); // White for values above upper bound
    }
    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);
    int g = (int) ((v - this.lowerBound) / (this.upperBound - this.lowerBound) * 255.0);
    return new Color(g, g, g);
}