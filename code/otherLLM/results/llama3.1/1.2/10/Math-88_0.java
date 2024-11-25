protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    if (basicRow == null) { // Add initialization check and handle edge case when getOriginalNumDecisionVariables() returns 0
        Arrays.fill(coefficients, 0);
        return new RealPointValuePair(coefficients, f.getValue(coefficients));
    }
    double mostNegative = getEntry(basicRow, getRhsOffset());
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow == null) { // Handle edge case when there are no basic rows
            coefficients[i] = 0;
        } else {
            coefficients[i] =
                (getEntry(basicRow, getRhsOffset())) - 
                (restrictToNonNegative ? 0 : mostNegative);
            if (basicRow != null) {
                for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                    if (tableau.getEntry(basicRow, j) == 1) {
                        coefficients[i] = 0;
                    }
                }
            }
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}