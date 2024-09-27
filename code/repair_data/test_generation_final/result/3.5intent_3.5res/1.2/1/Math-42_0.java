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
          coefficients[i] = 0 - (restrictToNonNegative ? 0 : mostNegative);
      } else {
          double entry = getEntry(basicRow, getRhsOffset());
          if (entry < 0) {
              if (restrictToNonNegative && basicRow != negativeVarBasicRow) {
                  coefficients[i] = 0;
              } else {
                  basicRows.add(basicRow);
                  coefficients[i] = entry - (restrictToNonNegative ? 0 : mostNegative);
              }
          } else {
              basicRows.add(basicRow);
              coefficients[i] = entry - (restrictToNonNegative ? 0 : mostNegative);
          }
      }
  }
  return new RealPointValuePair(coefficients, f.getValue(coefficients));
}