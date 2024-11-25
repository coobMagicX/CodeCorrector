public Paint getPaint(double value) {
    if (value < this.lowerBound) {
        // Return black color for values below the lower bound
        return new Color(0, 0, 0);
    } else if (value > this.upperBound) {
        // Return white color for values above the upper bound
        return new Color(255, 255, 255);
    } else {
        // Calculate grayscale value between black and white for values within bounds
        double v = Math.max(value, this.lowerBound);
        v = Math.min(v, this.upperBound);
        int g = (int) ((value - this.lowerBound) / (this.upperBound - this.lowerBound) * 255.0);
        return new Color(g, g, g);
    }
}