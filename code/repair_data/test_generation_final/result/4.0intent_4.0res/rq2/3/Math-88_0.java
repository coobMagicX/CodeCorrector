protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow;
    double mostNegative = Double.MAX_VALUE;

    // First pass to find the most negative coefficient among the basic variables
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            double value = getEntry(basicRow, getRhsOffset());
            if (value < mostNegative) {
                mostNegative = value;
            }
        }
    }
    
    // Second pass to calculate the coefficients
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow == null) {
            coefficients[i] = 0;
        } else {
            double value = getEntry(basicRow, getRhsOffset());
            coefficients[i] = value - (restrictToNonNegative ? 0 : mostNegative);
            // Ensuring only one basic variable can represent a column
            for (int j = 0; j < coefficients.length; j++) {
                if (j != i && tableau.getEntry(basicRow, getNumObjectiveFunctions() + j) == 1) {
                    coefficients[i] = 0;
                    break;
                }
            }
        }
    }
    
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}