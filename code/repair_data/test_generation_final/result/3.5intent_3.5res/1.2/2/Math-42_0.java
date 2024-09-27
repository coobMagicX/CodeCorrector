protected RealPointValuePair getSolution() {
  int negativeVarColumn = columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
  Integer negativeVarBasicRow = negativeVarColumn >= 0 ? getBasicRow(negativeVarColumn) : null; // Fixed condition
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
      if (basicRows.contains(basicRow)) {
          // if multiple variables can take a given value
          // then we choose the first and set the rest equal to 0
          coefficients[i] = Math.max(0, 0 - (restrictToNonNegative ? 0 : mostNegative)); // Ensuring non-negativity
      } else {
          basicRows.add(basicRow);
          coefficients[i] = Math.max(0, (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) -
              (restrictToNonNegative ? 0 : mostNegative)); // Ensuring non-negativity
      }
  }
  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}