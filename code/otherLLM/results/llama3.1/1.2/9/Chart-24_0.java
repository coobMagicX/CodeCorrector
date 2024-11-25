public Paint getPaint(double value) {
    double v = Math.max(value, this.getLowerBound());
    int g = (int) ((v - this.getLowerBound()) / (this.upperBound 
            - this.getLowerBound()) * 255.0);
    return new Color(g, g, g);
}