protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
    
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        
        // If the variable corresponding to the current index is a basic variable,
        // the coefficient should reflect its value from the tableau.
        double valueFromTableau = (basicRow == null) ? 0 : getEntry(basicRow, getRhsOffset());
        
        // Adjust for non-negativity constraint if applicable
        double adjustment = restrictToNonNegative ? mostNegative : 0;
        
        coefficients[i] = valueFromTableau - adjustment;
        
        // If the current variable is basic (has a coefficient of 1 in the tableau),
        // set its coefficient to 0.
        if (basicRow != null) {
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (MathUtils.equals(tableau.getEntry(basicRow, j), 1.0, epsilon)) {
                    coefficients[i] = 0;
                    break; // No need to continue checking other variables
                }
            }
        }
    }
    
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}