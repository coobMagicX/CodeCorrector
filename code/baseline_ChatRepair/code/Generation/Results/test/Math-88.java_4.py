protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    double mostNegative = Double.POSITIVE_INFINITY;
    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            double value = getEntry(basicRow, getRhsOffset());
            coefficients[i] = value;
            mostNegative = Math.min(mostNegative, value);
        } else {
            coefficients[i] = 0;
        }
    }
    
    // Adjust all coefficients if the model is not restricted to non-negative values
    if (!restrictToNonNegative && mostNegative < 0) {
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] -= mostNegative;
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
