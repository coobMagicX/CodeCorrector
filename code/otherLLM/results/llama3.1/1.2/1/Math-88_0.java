protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());

    // Choose the first variable to be non-zero if multiple variables can take a given value
    int chosenVariableIndex = -1;
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        double coefficient =
            (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) -
            (restrictToNonNegative ? 0 : mostNegative);
        
        if (basicRow != null) {
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (tableau.getEntry(basicRow, j) == 1) {
                    coefficient = 0;
                }
            }
        }

        // Set chosen variable to the most negative value
        if (chosenVariableIndex != -1 && chosenVariableIndex < i) {
            coefficients[i] = 0;
        } else {
            coefficients[chosenVariableIndex = i] = coefficient;
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}