protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());

    // Initialize all coefficients to the most negative value before applying adjustments
    for (int i = 0; i < coefficients.length; i++) {
        coefficients[i] = restrictToNonNegative ? 0 : mostNegative;
    }

    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null) {
            double valueAtBasicRow = getEntry(basicRow, getRhsOffset());
            // Adjust the coefficient based on the value at the basic row
            coefficients[i] += restrictToNonNegative ? 0 : valueAtBasicRow;

            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (tableau.getEntry(basicRow, j) == 1 && i != j - getNumObjectiveFunctions()) {
                    // If the basic row has a unit value in this column and it is not for the variable itself,
                    // set the coefficient to zero.
                    coefficients[i] = 0;
                }
            }
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}