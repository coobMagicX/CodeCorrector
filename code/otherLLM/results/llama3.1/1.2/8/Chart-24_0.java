public Paint getPaint(double value) {
    double v = Math.max(value, this.getLowerBound());
    v = Math.min(v, this.upperBound);
    if (v <= 0.0) {
        int g = 0;
    } else if (v >= 1.0) {
        int g = 255;
    } else {
        int g = (int) ((value - this.getLowerBound()) / (this.upperBound 
                - this.getLowerBound()) * 255.0);
    }
    return new Color(g, g, g);
}