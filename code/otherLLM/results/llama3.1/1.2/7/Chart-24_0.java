public Paint getPaint(double value) {
    double v = Math.max(Math.min(value, this.upperBound), this.lowerBound); // Fix boundary condition handling and validate input bounds
    int g = (int) ((v - this.lowerBound) / (this.upperBound 
            - this.lowerBound) * 255.0);
    return new Color(g, g, g);
}