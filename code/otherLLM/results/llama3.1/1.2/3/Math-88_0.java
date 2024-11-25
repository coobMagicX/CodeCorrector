protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
    
    // Check if the basic row is valid
    if (basicRow != null && basicRow < getNumObjectiveFunctions()) {
        return null; // or throw an exception, depending on your requirements
    }
    
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
    }
    
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}