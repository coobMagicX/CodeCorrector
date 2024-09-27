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
      if (basicRow == null) {
          coefficients[i] = 0;
      } else if (basicRows.contains(basicRow)) {
          coefficients[i] = 0;
      } else {
          basicRows.add(basicRow);
          coefficients[i] = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
      }
  }
  // Check if the solution is feasible regarding the negative variable constraint
  if (mostNegative < 0) {
    // When there is a negative variable, ensure the solution reflects it correctly
    for (int i = 0; i < coefficients.length; i++) {
      coefficients[i] -= mostNegative;
    }
  }
  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}