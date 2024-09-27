protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow;
    double mostNegative = Double.POSITIVE_INFINITY;
    
    // Iterate to find the most negative entry in the RHS
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            double entry = getEntry(basicRow, getRhsOffset());
            if (entry < mostNegative) {
                mostNegative = entry;
            }
        }
    }
    
    // Handle case when all entries are non-negative
    if (mostNegative > 0) {
        mostNegative = 0;
    }

    // Calculate coefficients
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        double entryValue = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset()));
        
        // Adjust coefficients based on non-negativity restriction
        coefficients[i] = entryValue - (restrictToNonNegative ? 0 : mostNegative);
        
        // Set coefficient to zero if the variable is not basic or if it's basic in a prior non-basic position
        if (basicRow != null) {
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (tableau.getEntry(basicRow, j) == 1) {
                    coefficients[i] = 0;
                    break;
                }
            }
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}