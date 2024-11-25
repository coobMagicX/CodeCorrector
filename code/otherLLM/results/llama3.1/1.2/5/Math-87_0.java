private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = 0; i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            return i;
        }
    }
    return null;
}