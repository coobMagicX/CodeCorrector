protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
    
    Set<Integer> selectedVars = new HashSet<>();
    for (int i = 0; i < coefficients.length; i++) {
        Integer row = getBasicRow(getNumObjectiveFunctions() + i);
        
        // if multiple variables can take a given value 
        // then we choose the first and set the rest equal to 0
        double coefficient;
        if (row == null) {
            coefficient = mostNegative - (restrictToNonNegative ? 0 : getEntry(basicRow, getRhsOffset()));
        } else {
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (tableau.getEntry(row, j) == 1) {
                    coefficient = getEntry(row, getOriginalNumDecisionVariables());
                    selectedVars.add(j);
                    break;
                }
            }
        }
        
        coefficients[i] = coefficient;
    }

    // set the rest of variables to zero
    for (int i = 0; i < coefficients.length; i++) {
        if (!selectedVars.contains(i)) {
            coefficients[i] = 0;
        }
    }
    
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}