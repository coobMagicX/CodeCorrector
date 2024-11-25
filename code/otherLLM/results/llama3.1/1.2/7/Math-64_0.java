private void checkSuccessfulIteration(double ratio) {
    if (ratio <= 0.25) {
        // update delta and lmPar
    } else if ((lmPar == 0) || (ratio >= 0.75)) {
        // update delta and lmPar
    }
}

private double calculateScaledActualReduction() {
    // logic to calculate actRed
}

private void testForSuccessfulIteration(double ratio, boolean firstIteration) {
    if (ratio >= 1.0e-4) {
        // successful iteration logic
        firstIteration = false;
        xNorm = 0;
        for (int k = 0; k < cols; ++k) {
            double xK = diag[k] * point[k];
            xNorm += xK * xK;
        }
        xNorm = Math.sqrt(xNorm);
    } else {
        // failed iteration logic
    }
}

// Usage:
if (checker == null) {
    if (((Math.abs(actRed) <= costRelativeTolerance) &&
            (preRed <= costRelativeTolerance) &&
            (ratio <= 2.0)) ||
           (delta <= parRelativeTolerance * xNorm)) {
        return current;
    }
} else {
    testForSuccessfulIteration(ratio, firstIteration);
}