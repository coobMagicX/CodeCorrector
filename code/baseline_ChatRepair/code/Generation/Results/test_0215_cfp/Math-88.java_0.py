protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow;
    double mostNegative = 0.0;

    // Find the most negative value for RHS entries among basic variables
    for (int i = 0; i < getOriginalNumDecisionVariables(); i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            double entry = getEntry(basicRow, getRhsOffset());
            if (entry < mostNegative) {
                mostNegative = entry;
            }
        }
    }

    // Set coefficients for the solution
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            // Only set coefficient to non-zero if it truly corresponds to a basic variable
            coefficients[i] = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
            for (int j = 0; j < i; j++) {
                Integer otherBasicRow = getBasicRow(getNumObjectiveFunctions() + j);
                if (otherBasicRow != null && tableau.getEntry(basicRow, getNumObjectiveFunctions() + j) == 1) {
                    coefficients[i] = 0;
                    break;
                }
            }
        } else {
            coefficients[i] = 0;
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
