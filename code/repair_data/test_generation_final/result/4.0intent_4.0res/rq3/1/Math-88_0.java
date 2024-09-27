protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow;
    double mostNegative = Double.MAX_VALUE;

    // Calculate the most negative entry in the right-hand side across all basic rows
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            double entry = getEntry(basicRow, getRhsOffset());
            if (entry < mostNegative) {
                mostNegative = entry;
            }
        }
    }

    // Adjust the most negative value for non-negative restrictions
    if (restrictToNonNegative && mostNegative < 0) {
        mostNegative = 0;
    }

    // Compute coefficients
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow == null) {
            coefficients[i] = 0;
        } else {
            coefficients[i] = getEntry(basicRow, getRhsOffset()) - mostNegative;
            // Check for other basic variables in this column and reset if found
            for (int j = 0; j < i; j++) {
                if (tableau.getEntry(basicRow, getNumObjectiveFunctions() + j) == 1) {
                    coefficients[i] = 0;
                    break;
                }
            }
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}