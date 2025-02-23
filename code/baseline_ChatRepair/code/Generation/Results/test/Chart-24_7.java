public Paint getPaint(double value) {
    double clampedValue = Math.max(value, this.lowerBound);
    clampedValue = Math.min(clampedValue, this.upperBound);
    double normalizedValue = (clampedValue - this.lowerBound) / (this.upperBound - this.lowerBound);
    int g = (int) (normalizedValue * 255.0);
    