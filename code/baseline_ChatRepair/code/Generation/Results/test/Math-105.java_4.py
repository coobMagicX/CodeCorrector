public double getSumSquaredErrors() {
    double sumSquaredXY = (sumXY * sumXY / sumXX);
    if (sumYY > sumSquaredXY) {
        return sumYY - sumSquaredXY;
    } else {
        return 0.0; 