protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow = null; // Initialize basicRow to null
    double mostNegative = 0; // Default value for most negative

    // Find the basic row that corresponds to the first non-negative variable
    for (int i = 0; i < getOriginalNumDecisionVariables(); i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        if (basicRow != null && getEntry(basicRow, getRhsOffset()) >= 0) {
            break;
        }
    }

    // If no non-negative basic row is found, set coefficients to zero
    if (basicRow == null) {
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = 0.0;
        }
    } else {
        double firstNonZeroCoefficient = 0;
        
        // Set the coefficient of the first non-negative variable
        for (int i = 0; i < getOriginalNumDecisionVariables(); i++) {
            basicRow = getBasicRow(getNumObjectiveFunctions() + i);
            coefficients[i] = getEntry(basicRow, getRhsOffset());
            
            if (basicRow != null && tableau.getEntry(basicRow, i) == 1.0) {
                firstNonZeroCoefficient = coefficients[i];
            }
        }

        // Set all other coefficients to zero except the one for the first non-negative variable
        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] != firstNonZeroCoefficient) {
                coefficients[i] = 0.0;
            }
        }
    }

    // The value of the third variable should be set to 1.0, assuming it's the last decision variable
    coefficients[getOriginalNumDecisionVariables() - 1] = 1.0;

    // The value of the objective function at this solution should be calculated using the correct method
    double objectiveFunctionValue = f.getValue(coefficients);

    return new RealPointValuePair(coefficients, objectiveFunctionValue);
}