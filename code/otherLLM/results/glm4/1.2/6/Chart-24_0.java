public Paint getPaint(double value) {
    if (value < this.getLowerBound()) {
        // Return black for values below the lower bound
        return new Color(0, 0, 0);
    } else if (value > this.getUpperBound()) {
        // Return white for values above or equal to the upper bound
        return new Color(255, 255, 255);
    } else {
        // Calculate grayscale value within the bounds
        double v = Math.max(value, this.getLowerBound());
        v = Math.min(v, this.getUpperBound());
        int g = (int) ((v - this.getLowerBound()) / (this.getUpperBound() - this.getLowerBound()) * 255.0);
        return new Color(g, g, g);
    }
}