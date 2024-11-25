private Integer getBasicRow(final int col) {
    Integer row = null;
    // Assuming getHeight() returns the total number of rows, including slack variables,
    // artificial variables, and constraints.
    for (int i = 0; i < getHeight(); i++) { // Starting from the first constraint row.
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                // If this is the first non-zero entry found in the basis, set it as the basic row.
                row = i;
            } else {
                // If there are already a previous non-zero entry, it means the variable
                // associated with this column has more than one constraint on the basis,
                // which should not happen in a Simplex tableau if it's been properly setup.
                // Therefore, we could throw an exception or return a special value indicating
                // the issue.
                throw new IllegalStateException("Multiple constraints on the same variable.");
            }
        }
    }
    // If no non-zero entry is found, this might indicate that the column is not part of the current basis.
    if (row == null) {
        // Depending on the solver's design, we may want to return a special value or throw an exception.
        throw new IllegalStateException("No constraint for the variable in the current basis.");
    }
    return row;
}