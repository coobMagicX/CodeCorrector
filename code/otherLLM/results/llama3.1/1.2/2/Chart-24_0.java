public Paint getPaint(double value) {
    if (value < this.lowerBound) {
        v = this.lowerBound; // handle edge case where value is less than lowerBound
    }
    double v = Math.max(value, this.lowerBound);
    v = Math.min(v, this.upperBound);
    int g = (int) ((v - this.lowerBound) / (this.upperBound 
            - this.lowerBound) * 255.0); // use 'v' instead of 'value'
    return new Color(g, g, g);
}