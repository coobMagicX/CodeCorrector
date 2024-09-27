protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow;
    double mostNegative = Double.POSITIVE_INFINITY;

    // First calculate all coefficients and find the most negative one if needed
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            coefficients[i] = getEntry(basicRow, getRhsOffset());
            if (!restrictToNonNegative && coefficients[i] < mostNegative) {
                mostNegative = coefficients[i];
            }
        } else {
            coefficients[i] = 0;
        }
    }

    // Adjust coefficients if non-negativity is not restricted
    if (!restrictToNonNegative && mostNegative < 0) {
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] -= mostNegative;
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}