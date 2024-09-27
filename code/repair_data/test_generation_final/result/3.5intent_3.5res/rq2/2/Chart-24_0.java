public Paint getPaint(double value) {
    double v = Math.max(value, getLowerBound());
    v = Math.min(v, getUpperBound());
    int g = (int) ((v - getLowerBound()) / (getUpperBound() - getLowerBound()) * 255.0);
    return new Color(g, g, g);
}