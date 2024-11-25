private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = 0; i < getHeight(); i++) { // Start from 0 to cover the full height range
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i; // Set the row when first non-zero entry is found
            } else {
                return null; // If a second non-zero entry is found, indicate ambiguity by returning null
            }
        }
    }
    return row;
}