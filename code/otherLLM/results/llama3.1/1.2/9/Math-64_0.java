public static double calculateCost(double[] residuals) {
    // Calculate the cost based on the residuals
    // For simplicity, let's assume it's the square of the sum of residuals
    double cost = 0;
    for (double residual : residuals) {
        cost += residual * residual;
    }
    return cost;
}

public static boolean checkForOverflow(double value1, double value2, double minTolerance) {
    // Check if adding two values would result in a NaN or overflow
    double sum = value1 + value2;
    if (Double.isNaN(sum)) {
        return true; // Overflow or NaN detected
    }
    if (Math.abs(sum - value1) < minTolerance && Math.abs(sum - value2) < minTolerance) {
        return true; // Sum is within tolerance of both values
    }
    return false;
}

public static double calculateScaledActualReduction(double previousCost, double actualCost) {
    // Calculate the scaled actual reduction
    if (actualCost == 0) {
        return -1.0; // No reduction when cost is zero
    }
    double relativeTolerance = 0.1 * previousCost / actualCost;
    return Math.max(-relativeTolerance, Math.min(actualCost - previousCost, 0));
}

public static double calculateScaledPredictedReduction(double lmPar, double lmNorm, double previousCost) {
    // Calculate the scaled predicted reduction
    double coeff1 = (lmPar * lmNorm * lmNorm / (previousCost * previousCost));
    return Math.max(0, -coeff1);
}