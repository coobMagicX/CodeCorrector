protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow;
    double mostNegative = 0;
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            coefficients[i] = getEntry(basicRow, getRhsOffset());
            // Check if there is exactly one entry of 1 in this column across all basic rows and ensure no other non-zero entries
            boolean uniqueOne = true;
            for (int j = getNumObjectiveFunctions(); j < getHeight(); j++) {
                if (j != basicRow && (MathUtils.equals(tableau.getEntry(j, getNumObjectiveFunctions() + i), 1.0, epsilon) ||
                                      !MathUtils.equals(tableau.getEntry(j, getNumObjectiveFunctions() + i), 0.0, epsilon))) {
                    uniqueOne = false;
                    break;
                }
            }
            if (!uniqueOne) {
                coefficients[i] = 0;
            }
        } else {
            coefficients[i] = 0;
        }
        if (i == 0 || (basicRow != null && getEntry(basicRow, getRhsOffset()) < mostNegative)) {
            mostNegative = getEntry(basicRow, getRhsOffset());
        }
    }
    if (!restrictToNonNegative) {
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] -= mostNegative;
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}