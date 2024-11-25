public Paint getPaint(double value) {
    double v = Math.max(value, this.getLowerBound());
    v = Math.min(v, this.getUpperBound());
    int g = (int) ((v - this.getLowerBound()) / (this.getUpperBound() 
            - this.getLowerBound()) * 255.0);
    return new Color(g, g, g);
}