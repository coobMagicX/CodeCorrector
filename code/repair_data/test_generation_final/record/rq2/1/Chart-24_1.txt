public Paint getPaint(double value) {
    double v = Math.max(value, getLowerBound());
    v = Math.min(v, this.upperBound);
    int g = (int) ((v - getLowerBound()) / (this.upperBound - getLowerBound()) * 255.0);
    return new Color(g, g, g);
}