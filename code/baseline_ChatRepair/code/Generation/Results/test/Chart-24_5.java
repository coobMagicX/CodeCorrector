public Paint getPaint(double value) {
    
    double clampedValue = Math.max(Math.min(value, this.upperBound), this.lowerBound);
    
    
    int g =