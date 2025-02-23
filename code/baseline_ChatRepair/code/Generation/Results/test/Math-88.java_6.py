protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow;
    double mostNegative;

    if (restrictToNonNegative) {
        mostNegative = 0;
    } else {
        mostNegative = Double.MAX_VALUE;
        for (int i = 0; i < getNumObjectiveFunctions() + getOriginalNumDecisionVariables(); i++) {
            basicRow = getBasicRow(i);
            if (basicRow != null) {
                double value = getEntry(basicRow, getRhsOffset());
                if (value < mostNegative) {
                    mostNegative = value;
                }
            }
        }
    }

    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        coefficients[i] = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) - (restrictToNonNegative ? 0 : mostNegative);
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
