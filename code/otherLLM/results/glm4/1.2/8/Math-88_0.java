protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow;
    double mostNegative;

    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            mostNegative = getEntry(basicRow, getRhsOffset());
        } else {
            mostNegative = 0;
        }
        
        // Set the coefficient for the current variable
        double entryValue = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
        coefficients[i] = (restrictToNonNegative ? 0 : -mostNegative) + entryValue;

        // If the tableau entry is 1, the variable is at its lower bound and should be set to 0
        if (tableau.getEntry(basicRow, getNumObjectiveFunctions() + i) == 1) {
            coefficients[i] = 0;
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}