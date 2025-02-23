public Paint getPaint(double value) {
    double clampedValue = Math.max(value, this.lowerBound);
    clampedValue = Math.min(clampedValue, this.upperBound);
    int g = (int) ((clampedValue - this.lowerBound) / (this.upperBound - this.lowerBound) * 255.0);
    g = Math.min(Math.max(g,