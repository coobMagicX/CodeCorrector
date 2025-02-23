protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    
    for (int i = 0; i < coefficients.length; i++) {
        int column = getNumObjectiveFunctions() + i;
        Integer basicRow = getBasicRow(column);
        coefficients[i] = 0;  // Initialize to zero by default
        
        if (basicRow != null) {
            double coefficientValue = getEntry(basicRow, getRhsOffset());
            boolean isBasicVariable = true;
            
            for (int j = 0; j < coefficients.length; j++) {
                if (i != j && tableau.getEntry(basicRow, getNumObjectiveFunctions() + j) != 0) {
                    isBasicVariable = false;
                    break;
                }
            }
            
            if (isBasicVariable) {
                coefficients[i] = coefficientValue;
            }
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}
