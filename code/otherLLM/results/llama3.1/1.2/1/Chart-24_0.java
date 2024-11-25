public Paint getPaint(double value) {
    double v = Math.max(value, this.getLowerBound());
    v = Math.min(v, this.getUpperBound());
    if (v < this.getLowerBound()) {
        throw new IllegalArgumentException("Value is less than lower bound");
    } else if (v > this.getUpperBound()) {
        throw new IllegalArgumentException("Value is greater than upper bound");
    }
    int g = (int) ((value - this.getLowerBound()) / (this.getUpperBound() 
            - this.getLowerBound()) * 255.0);
    return new Color(g, g, g);
}