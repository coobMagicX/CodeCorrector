protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? Double.MAX_VALUE : getEntry(basicRow, getRhsOffset());
    
    for (int i = 0; i < coefficients.length; i++) {
        // Update the basic row to the current column index
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        
        double entryValue = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
        double valueForCoefficient;
        
        if (restrictToNonNegative) {
            valueForCoefficient = entryValue; // Non-negative constraint implies coefficients are non-negative
        } else {
            valueForCoefficient = entryValue - mostNegative; // Adjust by the most negative value in the tableau
        }
        
        // Check for pivot elements that would make this coefficient zero
        if (basicRow != null && i >= getNumObjectiveFunctions()) {
            for (int j = getNumObjectiveFunctions(); j <= i; j++) { // Only need to check up to current column index
                double pivotEntry = tableau.getEntry(basicRow, j);
                if (pivotEntry == 1) {
                    valueForCoefficient = 0; // A pivot in the current column makes this coefficient zero
                    break;
                }
            }
        }

        coefficients[i] = valueForCoefficient;

        // Special case for the second objective function
        if (i == getNumObjectiveFunctions() + 1 && Math.abs(coefficients[i] - 1.0) > 1e-6) {
            throw new AssertionError("Second objective function value is not equal to 1.0");
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}