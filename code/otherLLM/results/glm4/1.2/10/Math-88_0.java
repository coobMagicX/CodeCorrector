protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow =
        getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset()));
    
    for (int i = 0; i < coefficients.length; i++) {
        int currentBasicRow;
        if (i > 0) { // Start from the second variable since we're looking at the second objective function
            currentBasicRow = getBasicRow(getNumObjectiveFunctions() + i);
        } else {
            currentBasicRow = basicRow; // Use the initial value for the first variable
        }
        
        double entryValue = (currentBasicRow == null ? 0 : getEntry(currentBasicRow, getRhsOffset()));
        coefficients[i] =
                (restrictToNonNegative ? entryValue - mostNegative : 0);

        if (currentBasicRow != null) {
            boolean isAtBasicRow = false;
            for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                if (tableau.getEntry(currentBasicRow, j) == 1) {
                    coefficients[i] = 0;
                    isAtBasicRow = true;
                    break;
                }
            }
            
            // For the second objective function, we need to consider equality constraints
            if (!isAtBasicRow && currentBasicRow != null) {
                for (int j = getNumObjectiveFunctions(); j < getNumObjectiveFunctions() + i; j++) {
                    if (tableau.getEntry(currentBasicRow, j) == 1) {
                        coefficients[i] = 0;
                        break;
                    }
                }
            }
        }
    }

    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}