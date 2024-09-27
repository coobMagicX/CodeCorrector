protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    double mostNegative = Double.MAX_VALUE;
    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        double value = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset()));
        coefficients[i] = value;
        if (value < mostNegative) {
            mostNegative = value;
        }
    }
    // Only adjust by mostNegative if restrictToNonNegative is false and mostNegative is less than 0
    if (!restrictToNonNegative && mostNegative < 0) {
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] -= mostNegative;
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}