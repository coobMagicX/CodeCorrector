protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
    
    // Ensure the coefficients array is correctly populated
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        
        // if multiple variables can take a given value 
        // then we choose the first and set the rest equal to 0
        coefficients[i] =
            (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) -
            (restrictToNonNegative ? 0 : mostNegative);
        
        if (basicRow != null) {
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (tableau.getEntry(basicRow, j) == 1) {
                    coefficients[i] = 0;
                }
            }
        }
        
        // Verify that the solution.getPoint()[2] value is calculated accurately
        double pointValue = f.getValue(coefficients);
        if (pointValue != null && Double.compare(pointValue, solution.getPoint()[2]) != 0) {
            throw new AssertionError("Solution does not match expected value");
        }
    }
    
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}