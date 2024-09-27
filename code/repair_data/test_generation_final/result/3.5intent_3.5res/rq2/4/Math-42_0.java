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
      if (basicRows.contains(basicRow) || (basicRow != null && getEntry(basicRow, getRhsOffset()) < 0)) {
          coefficients[i] = 0;
      } else {
          basicRows.add(basicRow);
          coefficients[i] = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) - mostNegative;
      }
  }

  if (restrictToNonNegative) {
      for (int i = 0; i < coefficients.length; i++) {
          if (coefficients[i] < 0) {
              coefficients[i] = 0;
          }
      }
  }

  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}