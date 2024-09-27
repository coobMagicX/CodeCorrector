public Paint getPaint(double value) {
    double v = Math.max(value, getLowerBound());
    v = Math.min(v, getUpperBound());
    double range = getUpperBound() - getLowerBound();
    int g = (int) ((v - getLowerBound()) / range * 255.0);
    return new Color(g, g, g);
}