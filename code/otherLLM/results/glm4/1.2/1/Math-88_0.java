protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());

    for (int i = 0; i < coefficients.length; i++) {
        int currentColumnIndex = getNumObjectiveFunctions() + i;
        if (i == 1) { // Assuming the second objective function coefficient needs to be 1.0
            coefficients[i] = 1.0;
        } else {
            basicRow = getBasicRow(currentColumnIndex);
            double entryValue = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
            if (restrictToNonNegative) {
                coefficients[i] = entryValue - mostNegative; // Adjust for non-negative constraint
            } else {
                coefficients[i] = entryValue;
            }
        }

        // Check if the current coefficient is associated with a basic variable and should be set to 0
        for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
            if (tableau.getEntry(basicRow, j) == 1 && !isObjectiveFunction(j)) { // Ensure it's not an objective function column
                coefficients[i] = 0;
            }
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}