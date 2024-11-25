protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow = null;
    double mostNegative = 0;

    // Find the basic row and most negative value in the current tableau
    for (int i = 0; i < getNumConstraints(); i++) { // Assuming getNumConstraints() is defined to iterate over all constraints
        if (getBasicRow(i) != null && (basicRow == null || getEntry(basicRow, getRhsOffset()) > getEntry(getBasicRow(i), getRhsOffset()))) {
            basicRow = getBasicRow(i);
            mostNegative = getEntry(basicRow, getRhsOffset());
        }
    }

    // Compute coefficients for decision variables
    for (int i = 0; i < coefficients.length; i++) {
        double variableCoefficient = 0;

        if ((basicRow != null) && getBasicRow(getNumObjectiveFunctions() + i).equals(basicRow)) {
            variableCoefficient = mostNegative;
        } else if ((basicRow != null) && (i < getNumConstraints())) {
            variableCoefficient = getEntry(basicRow, getRhsOffset()) - getEntry(basicRow, getRhsOffset());
        }

        // Adjust coefficient based on whether we are restricting to non-negative values
        if (restrictToNonNegative) {
            variableCoefficient = Math.max(variableCoefficient, 0);
        }

        coefficients[i] = variableCoefficient;

        // If the current basic row corresponds to a constraint with a pivot in the decision variable's column,
        // set its coefficient to zero.
        if ((basicRow != null) && tableau.getEntry(basicRow, getNumObjectiveFunctions() + i) == 1) {
            coefficients[i] = 0;
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}