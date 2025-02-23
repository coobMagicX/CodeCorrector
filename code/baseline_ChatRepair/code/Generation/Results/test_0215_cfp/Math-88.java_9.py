protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        
        // Set coefficient based on the basic row identification and handling negative constraints
        if (basicRow != null) {
            // Observing entire row to decide if the coefficient should exist as non-zero
            boolean isFree = true;
            for (int j = 0; j < getNumObjectiveFunctions() + getOriginalNumDecisionVariables(); j++) {
                if (j != getNumObjectiveFunctions() + i && tableau.getEntry(basicRow, j) != 0) {
                    isFree = false;
                    break;
                }
            }
            if (isFree) {
                coefficients[i] = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
            } else {
                coefficients[i] = 0;
            }
        } else {
            coefficients[i] = 0;
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
