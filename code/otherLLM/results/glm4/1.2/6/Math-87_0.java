private Integer getBasicRow(final int col) {
    Integer row = null;
    boolean firstNonZeroFound = false;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (!firstNonZeroFound) {
                row = i;
                firstNonZeroFound = true;
            } else {
                return null; // This is fine as the test case expects multiple non-zero entries.
            }
        }
    }
    return firstNonZeroFound ? row : null; // Return the found row or null if no non-zero entry was found.
}