protected RealPointValuePair getSolution() {
  int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
  Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
  double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());

  Set<Integer> basicRows = new HashSet<Integer>();
  double[] coefficients = new double[getOriginalNumDecisionVariables()];
  for (int i = 0; i < coefficients.length; i++) {
      int colIndex = columnLabels.indexOf("x" + i);
      if (colIndex < 0) {
        coefficients[i] = 0;
        continue;
      }
      Integer basicRow = getBasicRow(colIndex);
          // if the basic row is found to be the objective function row
          // set the coefficient to 0 -> this case handles unconstrained 
          // variables that are still part of the objective function
      if (basicRows.contains(basicRow)) {
          // if multiple variables can take a given value
          // then we choose the first and set the rest equal to 0
          coefficients[i] = mostNegative > 0 ? mostNegative : 0 - mostNegative;
      } else {
          basicRows.add(basicRow);
          coefficients[i] =
              (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) -
              (restrictToNonNegative ? 0 : mostNegative);
      }
  }
  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}