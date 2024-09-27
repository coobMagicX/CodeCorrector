protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        coefficients[i] = 0; // Initialize coefficients to zero by default

        if (basicRow != null) {
            boolean isDistinctBasicVariable = true;
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + getOriginalNumDecisionVariables(); j++) {
                if ((tableau.getEntry(basicRow, j) != 0) && (j != getNumObjectiveFunctions() + i)) {
                    isDistinctBasicVariable = false;
                    break;
                }
            }
            if (isDistinctBasicVariable) {
                coefficients[i] = getEntry(basicRow, getRhsOffset()); 
                // Ensure non-negative restriction is being considered
                if (restrictToNonNegative && coefficients[i] < 0) {
                    coefficients[i] = 0;
                }
            }
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}